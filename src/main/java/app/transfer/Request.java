package app.transfer;

import app.model.HumanBeing;

import java.util.List;

/**
 * Request record encapsulates the command to execute.
 */
public record Request(String command, List<String> args, List<HumanBeing> persons) {
}