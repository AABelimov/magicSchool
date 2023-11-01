package hogwarts.ru.magicschool.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("info")
public class InfoController {

    private final int serverPort;

    public InfoController(@Value("${server.port}") int serverPort) {
        this.serverPort = serverPort;
    }

    @GetMapping("port")
    public int getServerPort() {
        return serverPort;
    }
}
