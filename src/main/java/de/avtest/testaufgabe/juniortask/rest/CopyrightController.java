package de.avtest.testaufgabe.juniortask.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/copyright")
public class CopyrightController {

  String gameName = """
           _____ _        _____             _____          \s
          /__   (_) ___  /__   \\__ _  ___  /__   \\___   ___\s
            / /\\/ |/ __|   / /\\/ _` |/ __|   / /\\/ _ \\ / _ \\
           / /  | | (__   / / | (_| | (__   / / | (_) |  __/
           \\/   |_|\\___|  \\/   \\__,_|\\___|  \\/   \\___/ \\___|
                                                           \s
          """;


  String myName = """
                                                                  \s
          ,--.                        ,--.         ,--.          \s
          |  |-.  ,--. ,--.    ,---.  |  |  ,---.  `--' ,--.  ,--.\s
          | .-. '  \\  '  /    | .-. | |  | | .-. | ,--.  \\  `'  / \s
          | `-' |   \\   '     ' '-' | |  | ' '-' ' |  |   \\    /  \s
           `---'  .-'  /       `-|  | `--'  `---'  `--'    `--'   \s
                  `---'          `--'                             \s
          """;

  @GetMapping(produces = "text/plain")
  public String getCopyright() {
    return gameName + myName;
  }

}
