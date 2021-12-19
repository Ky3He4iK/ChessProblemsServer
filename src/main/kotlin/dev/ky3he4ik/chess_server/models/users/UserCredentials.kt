package dev.ky3he4ik.chess_server.models.users

import com.fasterxml.jackson.annotation.JsonManagedReference
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "user_credentials")
class UserCredentials(
    @Id
    @GeneratedValue
    @Column
    var userId: UUID? = null,
    @Column(nullable = false)
    var login: String? = null,
    @Column(nullable = false)
    var password: String? = null,
    @Column
    var role: Role = Role.USER,
    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonManagedReference
    var user: UserInfo = UserInfo()
) {
    enum class Role {
        ADMIN, MODERATOR, PREMIUM, USER
    }
}
