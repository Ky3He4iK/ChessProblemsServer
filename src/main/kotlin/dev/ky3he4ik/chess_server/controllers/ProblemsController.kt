package dev.ky3he4ik.chess_server.controllers
//
//import dev.ky3he4ik.chess_server.db.dao.ProblemInfoDAO
//import dev.ky3he4ik.chess_server.db.dao.UserCredentialsDAO
//import dev.ky3he4ik.chess_server.db.dao.UserInfoDAO
//import dev.ky3he4ik.chess_server.models.problems.ProblemInfo
//import dev.ky3he4ik.chess_server.security.services.JWTUtils
//import dev.ky3he4ik.chess_server.security.services.MyUserDetailsService
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.http.ResponseEntity
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.core.Authentication
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//class ProblemsController {
//    @Autowired
//    private lateinit var authenticationManager: AuthenticationManager
//
//    @Autowired
//    private lateinit var passwordEncoder: PasswordEncoder
//
//    @Autowired
//    private lateinit var userDetailsService: MyUserDetailsService
//
//    @Autowired
//    private lateinit var jwtUtils: JWTUtils
//
//    @Autowired
//    private lateinit var userCredentialsDAO: UserCredentialsDAO
//
//    @Autowired
//    private lateinit var userInfoDAO: UserInfoDAO
//
//    @Autowired
//    private lateinit var problemInfoDAO: ProblemInfoDAO
//
//
//
//    @GetMapping("/problems/random")
//    fun getRandomProblem(authentication: Authentication): ResponseEntity<ProblemInfo?> {
//        val user = userCredentialsDAO.findByLoginLike(authentication.name)
//
//
//
//        runBlocking {
//            val job = GlobalScope.launch {
//                if (profile != null) {
//                    val res = common.getProfileInfo(profile.authToken!!)
//                    if (res.isSuccessful) {
//                        result = ResponseEntity.ok(res.body())
//                    }
//                }
//            }
//            job.join()
//        }
//        return result
//    }
//
//    @GetMapping("/problems/add")
//    fun addProblem(authentication: Authentication): ResponseEntity<ProblemInfo?> {
//        val user =
//            authentication.name
//        val profile: UserInfo? = authenticationManager.findByAuthTokenLike(authentication.name)
//
//        runBlocking {
//            val job = GlobalScope.launch {
//                if (profile != null) {
//                    val res = common.getProfileInfo(profile.authToken!!)
//                    if (res.isSuccessful) {
//                        result = ResponseEntity.ok(res.body())
//                    }
//                }
//            }
//            job.join()
//        }
//        return result
//    }
//
//    fun gatherRandomProblem(): ProblemInfo {
//
//    }
//}