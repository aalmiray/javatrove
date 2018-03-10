package org.kordamp.javatrove.chat01.server;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Andres Almiray
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommandExecutionException extends Exception {
    public CommandExecutionException(Throwable cause) {
        super(cause);
    }
}
