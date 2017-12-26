package ru.dmitriymx.skeleton.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Здесь описываются все пути, что начинаются с '/'
 */
@Controller
@RequestMapping("/")
public class WebAppController {

    /**
     * Обработка корневого запроса '/'
     *
     * @return Сырое строковое значение
     */
    @RequestMapping
    @ResponseBody
    public String index() {
        return "Hello world!";
    }
}
