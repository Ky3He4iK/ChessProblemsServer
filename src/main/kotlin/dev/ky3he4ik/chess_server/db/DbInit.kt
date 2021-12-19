package dev.ky3he4ik.chess_server.db

import dev.ky3he4ik.chess_server.db.dao.ProblemInfoDAO
import dev.ky3he4ik.chess_server.db.dao.UserInfoDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DbInit @Autowired constructor(
    var problemInfoDAO: ProblemInfoDAO,
    var userInfoDAO: UserInfoDAO,
    var passwordEncoder: PasswordEncoder
) : ApplicationRunner {


    @Autowired
    fun dataInit(
        problemInfoDAO: ProblemInfoDAO,
        userInfoDAO: UserInfoDAO,
        passwordEncoder: PasswordEncoder,
    ) {
        this.problemInfoDAO = problemInfoDAO
        this.userInfoDAO = userInfoDAO
        this.passwordEncoder = passwordEncoder
    }

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
//        partyDAO.save(Party("Практическое занятие группы ИКБО-06-19"))
//        partyDAO.save(Party("Практическое занятие группы ИКБО-07-19"))
//        partyDAO.save(Party("Практическое занятие группы ИКБО-11-19"))
//        partyDAO.save(Party("Практическое занятие группы ИКБО-12-19"))
//        personCredentialsDAO.save(
//            PersonCredentials(
//                "admin",
//                passwordEncoder.encode("admin"),
//                PersonCredentials.Role.ADMIN,
//                personDAO.save(Person("admin", "admin"))
//            )
//        )
//        personCredentialsDAO.save(
//            PersonCredentials(
//                "moder",
//                passwordEncoder.encode("moder"),
//                PersonCredentials.Role.MODER,
//                personDAO.save(Person("moder", "moder"))
//            )
//        )
    }
}
