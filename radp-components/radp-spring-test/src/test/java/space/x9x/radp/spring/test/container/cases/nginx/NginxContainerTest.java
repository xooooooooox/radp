/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.spring.test.container.cases.nginx;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author x9x
 * @since 2025-05-24 16:52
 */
@Testcontainers
class NginxContainerTest {

    @Container
    private GenericContainer<?> nginx = new GenericContainer<>("nginx:1.21.6")
            .withExposedPorts(80)
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("volumes/nginx/index.html"),
                    "/usr/share/nginx/html/index.html"
            );

    @Test
    void testNginxServersCustomPage() throws IOException {
        String url = String.format("http://%s:%s", nginx.getHost(), nginx.getMappedPort(80));
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode); // 验证 Nginx 返回 200 OK
    }

}
