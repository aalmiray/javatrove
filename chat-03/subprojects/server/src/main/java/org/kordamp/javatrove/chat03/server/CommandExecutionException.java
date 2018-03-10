package org.kordamp.javatrove.chat03.server;

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
