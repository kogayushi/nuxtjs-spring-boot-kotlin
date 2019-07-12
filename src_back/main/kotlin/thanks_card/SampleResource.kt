package thanks_card

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections

@RequestMapping("/api")
@RestController
class SampleResource {
    @GetMapping(path = ["/hello"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun hello(): String {
        val shuffling = ArrayList(LIST)
        Collections.shuffle(shuffling)
        return shuffling[0]
    }

    companion object {
        private val LIST = Arrays.asList("太郎", "次郎", "三郎")
    }
}
