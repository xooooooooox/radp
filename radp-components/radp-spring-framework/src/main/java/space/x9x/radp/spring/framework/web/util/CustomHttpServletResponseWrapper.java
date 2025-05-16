package space.x9x.radp.spring.framework.web.util;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * HttpServletResponse 包装器
 *
 * @author x9x
 * @since 2024-09-27 20:55
 */
public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream outputStream;
    private final PrintWriter writer;

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response the {@link HttpServletResponse} to be wrapped.
     * @throws IllegalArgumentException if the response is null
     */
    public CustomHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream, true);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    /**
     * Returns the content of the response as a String.
     * This method converts the captured response bytes to a String using UTF-8 encoding.
     *
     * @return the content of the response as a String
     */
    public String getContent() {
        return outputStream.toString(StandardCharsets.UTF_8);
    }
}
