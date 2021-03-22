package com.github.nsc.de.shake;

import com.github.nsc.de.shake.cli.ShakeCli;
import com.github.nsc.de.shake.generators.java.JavaGenerator;
import com.github.nsc.de.shake.generators.java.nodes.JavaClass;
import com.github.nsc.de.shake.generators.json.JsonGenerator;
import com.github.nsc.de.shake.interpreter.Interpreter;
import com.github.nsc.de.shake.interpreter.values.InterpreterValue;
import com.github.nsc.de.shake.lexer.Lexer;
import com.github.nsc.de.shake.lexer.characterinput.characterinputstream.CharacterInputStream;
import com.github.nsc.de.shake.lexer.characterinput.characterinputstream.SourceCharacterInputStream;
import com.github.nsc.de.shake.lexer.characterinput.position.PositionMap;
import com.github.nsc.de.shake.lexer.token.TokenInputStream;
import com.github.nsc.de.shake.parser.Parser;
import com.github.nsc.de.shake.parser.node.Tree;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ShakeTests {

    private static File tempDir = new File("./target/shake/tests");
    private static File javaTemp;

    @BeforeAll
    public static void setupTests(@TempDir Path tmp) {

        // tempDir = new File(String.valueOf(tmp));
        javaTemp = new File(tempDir, "java");
        if(javaTemp.exists()) deleteRecursive(javaTemp);
        javaTemp.mkdirs();

    }

    @ParameterizedTest
    @MethodSource("testStream")
    public void interpreterTests(ShakeTest test) {
        assertEquals(test.getResult(), run(test.getSourceFile().getAbsolutePath(), test.getCode()).toString());
    }

    @ParameterizedTest
    @MethodSource("testStream")
    public void jsonTests(ShakeTest test) {
        assertTrue(test.getJson().similar(generateJson(test.getSourceFile().getAbsolutePath(), test.getCode())));
    }


    @ParameterizedTest
    @MethodSource("testStream")
    public void javaTests(ShakeTest test) {
        try {
            JavaClass cls = generateJava(test.getSourceFile().getAbsolutePath(), test.getCode());

            File directory = new File(javaTemp, Paths.get(test.getName()).getParent().toString());
            File javaFile = writeFile(new File(directory, cls.getName() + ".java"), cls.toString());
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

            int result = compiler.run(null, System.out, System.err, javaFile.toString());
            assertSame(0, result);

            Processes.executeClassInternal(directory.toString(), cls.getName(), new String[]{});
        } catch (Throwable t) {
            throw new ShakeTestJavaError(test, t);
        }

    }

    static Stream<ShakeTest> testStream() throws FileNotFoundException {
        List<ShakeTest> list = new ArrayList<>();
        Scanner reader = new Scanner(new File("src/test/resources/shake-tests/tests.txt"));
        while (reader.hasNextLine()) {
            list.add(new ShakeTest(reader.nextLine()));
        }
        return Stream.of(list.toArray(new ShakeTest[] {}));
    }

    public InterpreterValue run(String source, String code) {

        return run(parse(source, code));

    }

    public InterpreterValue run(ParseResult parsed) {

        return new Interpreter().visit(parsed.getTree());

    }

    public JSONObject generateJson(String source, String code) {

        return generateJson(parse(source, code));

    }

    public JSONObject generateJson(ParseResult parsed) {

        return new JsonGenerator().visitTree(parsed.getTree());

    }

    public JavaClass generateJava(String source, String code) {

        String baseName = source != null ?
                source.substring(0, source.length() - 6)
                        .replace("src/test/resources/shake-tests/tests/", "")
                        .split("[\\\\/](?=[^\\\\/]+$)")[1] : null;

        return generateJava(parse(source, code).getTree(), baseName);

    }

    public JavaClass generateJava(Tree tree, String className) {

        return new JavaGenerator().visitProgram(tree, className);

    }

    public ParseResult parse(String source, String code) {

        CharacterInputStream in = new SourceCharacterInputStream(source, code);
        Lexer lexer = new Lexer(in);
        TokenInputStream tokens = lexer.makeTokens();

        Parser parser = new Parser(tokens);
        return new ParseResult(parser.parse(), tokens.getMap());

    }



    public static class ShakeTest {

        private final String name;
        private final String code;
        private final String result;
        private final JSONObject json;

        ShakeTest(String name) throws FileNotFoundException {
            this.name = name;
            this.code = readFile(new File(String.format("src/test/resources/shake-tests/tests/%s.shake", name)));
            this.result = readFile(new File(String.format("src/test/resources/shake-tests/results/%s.txt", name)));
            this.json = new JSONObject(readFile(new File(String.format("src/test/resources/shake-tests/json/%s.json", name))));
        }

        public String getName() {
            return name;
        }

        public File getSourceFile() {
            return new File(String.format("src/test/resources/shake-tests/tests/%s.shake", this.getName()));
        }

        public File getResultFile() {
            return new File(String.format("src/test/resources/shake-tests/results/%s.txt", this.getName()));
        }

        public File getJsonFile() {
            return new File(String.format("src/test/resources/shake-tests/results/%s.json", this.getName()));
        }

        public String getCode() {
            return code;
        }

        public String getResult() {
            return result;
        }

        public JSONObject getJson() {
            return json;
        }

        private static String readFile(File f) throws FileNotFoundException {
            Scanner reader = new Scanner(f);
            StringBuilder ret = new StringBuilder();
            while (reader.hasNextLine()) {
                ret.append(reader.nextLine()).append('\n');
            }
            if(ret.length() > 0) ret.setLength(ret.length() - 1);

            reader.close();
            return ret.toString();
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    private static class ParseResult {

        private final Tree tree;
        private final PositionMap map;

        private ParseResult(Tree tree, PositionMap map) {
            this.tree = tree;
            this.map = map;
        }

        public Tree getTree() {
            return tree;
        }

        public PositionMap getMap() {
            return map;
        }
    }


    private static File writeFile(File f, String content) throws IOException {
        System.out.printf("Generating file \"%s\"...%n", f.getAbsolutePath());
        f.getParentFile().mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        writer.write(content);
        writer.close();
        return f;
    }

    private static class ShakeTestError extends Error {

        public ShakeTestError(String message, Throwable cause) {
            super(message, cause);
        }

        public ShakeTestError(String message) {
            super(message);
        }

    }

    private static class ShakeTestJavaError extends ShakeTestError {

        public ShakeTestJavaError(String message, Throwable cause) {
            super(message, cause);
        }

        public ShakeTestJavaError(String message) {
            super(message);
        }

        public ShakeTestJavaError(ShakeTest test, Throwable cause) {
            this(String.format("Error occurred while compiling test \"%s\" into java", test.getSourceFile().getPath()),
                    cause);
        }

        public ShakeTestJavaError(ShakeTest test) {
            this(String.format("Error occurred while compiling test \"%s\" into java", test.getSourceFile().getPath()));
        }

    }

    private static void deleteRecursive(File file) {
        if(file.isDirectory())
            for(File f : file.listFiles())
                deleteRecursive(f);
        file.delete();
    }

}
