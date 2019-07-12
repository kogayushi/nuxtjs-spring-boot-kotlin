package thanks_card

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Profile("local")
@Controller
class DummyController {

    @GetMapping("/**")
    fun dummyTop(): String {
        return "redirect:http://localhost:3000"
    }
}
