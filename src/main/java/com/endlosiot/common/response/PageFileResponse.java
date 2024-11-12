/**
 *
 */
package com.endlosiot.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.File;

/**
 * This is used to return file response.
 *
 * @author Core team.
 * @version 1.0
 * @since 08/12/2018
 */

@JsonInclude(Include.NON_NULL)
public class PageFileResponse extends CommonResponse {


    private static final long serialVersionUID = 2618791371942476644L;

    private File file;


    protected PageFileResponse(int responseCode, String message, File file) {
        super(responseCode, message);
        this.file = file;
    }

    public static PageFileResponse create(int responseCode, String message, File file) {
        return new PageFileResponse(responseCode, message, file);
    }

    public File getFile() {
        return file;
    }


}
