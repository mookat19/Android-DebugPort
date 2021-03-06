package jwf.debugport.internal.sqlite.commands;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jwf.debugport.annotations.Command;
import jwf.debugport.internal.sqlite.SQLiteClientConnection;

/**
 *
 */
@Command.Help(
        format = "USE [database name];",
        value = "Connect to the database called [database name]. All SQL commands will be executed against this database until USE is called again.",
        group = Command.GROUP_SQL_DATABASES
)
public class UseCommand extends SQLiteCommand {
    private static final Pattern command = Pattern.compile("\\s*use\\s+(\\w+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    @Override
    public boolean isCommandString(String candidate) {
        return command.matcher(candidate).matches();
    }

    @Override
    public void execute(SQLiteClientConnection connection, PrintWriter out, String commandString) {
        Matcher commandMatcher = command.matcher(commandString);
        if (!commandMatcher.matches()) {
            return;
        }
        String dbName = commandMatcher.group(1);

        if (!isValidDatabaseName(dbName)) {
            out.println("Invalid database name: "+dbName);
            out.flush();
        } else {
            if (!connection.databaseExists(dbName)) {
                out.println("Database `"+dbName+"` does not exist");
                return;
            }

            connection.setCurrentDatabase(dbName);
            out.println("Using database `"+dbName+"`");
        }
    }
}
