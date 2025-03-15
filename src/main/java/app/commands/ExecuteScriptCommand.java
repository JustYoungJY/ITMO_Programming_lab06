package app.commands;

import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;

/**
 * execute_script command: Reads and executes a script from the specified file.
 */
public class ExecuteScriptCommand implements Command {
    private final CommandInvoker invoker;
    private final InputReader reader;

    public ExecuteScriptCommand(CommandInvoker invoker, InputReader reader) {
        this.invoker = invoker;
        this.reader = reader;
    }

    @Override
    public Response execute(Request request) {
        String fileName;
        if (!request.args().isEmpty()) {
            fileName = request.args().get(0);
        } else {
            fileName = reader.prompt("Enter script file name: ");
        }
        // Сохраняем оригинальный Scanner
        Scanner originalScanner = reader.getScanner();
        try (Scanner scriptScanner = new Scanner(new File(fileName))) {
            // Заменяем Scanner в InputReader на тот, что читает из файла
            reader.setScanner(scriptScanner);
            while (scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                Response r = invoker.executeCommand(new Request(line, Collections.emptyList(), Collections.emptyList()));
                System.out.println(r.message());
            }
            return new Response("Script executed");
        } catch (FileNotFoundException e) {
            return new Response("Error: Script file not found.");
        } finally {
            // Восстанавливаем оригинальный Scanner, чтобы интерактивный режим снова работал
            reader.setScanner(originalScanner);
        }
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return "Read and execute a script from the specified file";
    }
}