package dev.ky3he4ik.chess_server.models.users

import com.fasterxml.jackson.annotation.JsonManagedReference
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "user_credentials")
class UserCredentials {
    @Id
    @GeneratedValue
    @Column
    var id: UUID? = null

    @Column(nullable = false)
    var login: String? = null

    @Column(nullable = false)
    var password: String? = null

    @Column(nullable = false)
    var salt: String? = null

    @Column
    val role: Role = Role.USER

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    var person: UserInfo? = null


    enum class Role {
        ADMIN, MODERATOR, PREMIUM, USER
    }
}
