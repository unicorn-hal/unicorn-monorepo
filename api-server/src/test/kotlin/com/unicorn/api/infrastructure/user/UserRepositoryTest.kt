package com.unicorn.api.infrastructure.user

import com.unicorn.api.domain.user.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@TestPropertySource(locations = ["classpath:application-test.properties"])
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql("/db/user/Insert_Parent_Account_Data.sql")
@Sql("/db/user/Insert_User_Data.sql")
@Sql("/db/user/Insert_Deleted_User_Data.sql")
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private fun findUserByUserID(userID: String): User? {
        // language=postgresql
        val sql = """
            SELECT
                user_id, 
                first_name, 
                last_name, 
                gender, 
                email, 
                birth_date, 
                address, 
                postal_code, 
                phone_number, 
                icon_image_url, 
                body_height, 
                body_weight, 
                occupation
            FROM users WHERE user_id = :userID AND deleted_at IS NULL
        """.trimIndent()

        val sqlParams = MapSqlParameterSource().addValue("userID", userID)

        return namedParameterJdbcTemplate.query(
            sql,
            sqlParams
        ) { rs, _ ->
            User.fromStore(
                userID = rs.getString("user_id"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                gender = rs.getString("gender"),
                email = rs.getString("email"),
                birthDate = rs.getObject("birth_date", LocalDate::class.java),
                address = rs.getString("address"),
                postalCode = rs.getString("postal_code"),
                phoneNumber = rs.getString("phone_number"),
                iconImageUrl = rs.getString("icon_image_url"),
                bodyHeight = rs.getDouble("body_height"),
                bodyWeight = rs.getDouble("body_weight"),
                occupation = rs.getString("occupation")
            )
        }.singleOrNull()
    }

    @Test
    fun `should store user`() {
        val user = User.create(
            userID = "12345",
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            birthDate = LocalDate.now().minusDays(1),
            gender = "male",
            address = "123 Main St",
            postalCode = "1234567",
            phoneNumber = "09012345678",
            iconImageUrl = "http://example.com/icon.png",
            bodyHeight = 180.0,
            bodyWeight = 75.0,
            occupation = "Engineer"
        )

        userRepository.store(user)

        val storedUser = findUserByUserID(user.userID.value)
        assert(storedUser?.userID == user.userID)
        assert(storedUser?.firstName == user.firstName)
        assert(storedUser?.lastName == user.lastName)
        assert(storedUser?.email == user.email)
        assert(storedUser?.birthDate == user.birthDate)
        assert(storedUser?.address == user.address)
        assert(storedUser?.postalCode == user.postalCode)
        assert(storedUser?.phoneNumber == user.phoneNumber)
        assert(storedUser?.iconImageUrl == user.iconImageUrl)
        assert(storedUser?.bodyHeight == user.bodyHeight)
        assert(storedUser?.bodyWeight == user.bodyWeight)
        assert(storedUser?.occupation == user.occupation)
    }

    @Test
    fun `should store user with already deleted userID`() {
        val user = User.create(
            userID = "testtest",
            firstName = "test",
            lastName = "test",
            gender = "male",
            email = "sample@test.com",
            birthDate = LocalDate.of(1990, 1, 1),
            address = "test",
            postalCode = "0000000",
            phoneNumber = "00000000000",
            iconImageUrl = "https://example.com",
            bodyHeight = 170.4,
            bodyWeight = 60.4,
            occupation = "test"
        )

        userRepository.store(user)

        val storedUser = findUserByUserID(user.userID.value)
        assert(storedUser?.userID == user.userID)
        assert(storedUser?.firstName == user.firstName)
        assert(storedUser?.lastName == user.lastName)
        assert(storedUser?.email == user.email)
        assert(storedUser?.birthDate == user.birthDate)
        assert(storedUser?.address == user.address)
        assert(storedUser?.postalCode == user.postalCode)
        assert(storedUser?.phoneNumber == user.phoneNumber)
        assert(storedUser?.iconImageUrl == user.iconImageUrl)
        assert(storedUser?.bodyHeight == user.bodyHeight)
        assert(storedUser?.bodyWeight == user.bodyWeight)
        assert(storedUser?.occupation == user.occupation)
    }

    @Test
    fun `should update user`() {
        val user = User.create(
            userID = "test",
            firstName = "testtest",
            lastName = "testtest",
            gender = "male",
            email = "sample@test.com",
            birthDate = LocalDate.of(1990, 1, 1),
            address = "test",
            postalCode = "0000000",
            phoneNumber = "00000000000",
            iconImageUrl = "https://example.com",
            bodyHeight = 170.4,
            bodyWeight = 60.4,
            occupation = "test"
        )

        // storeでアップデート
        userRepository.store(user)

        val result = findUserByUserID(user.userID.value)
        assert(result?.firstName == user.firstName)
        assert(result?.lastName == user.lastName)
        assert(result?.email == user.email)
        assert(result?.birthDate == user.birthDate)
        assert(result?.address == user.address)
        assert(result?.postalCode == user.postalCode)
        assert(result?.phoneNumber == user.phoneNumber)
        assert(result?.iconImageUrl == user.iconImageUrl)
        assert(result?.bodyHeight == user.bodyHeight)
        assert(result?.bodyWeight == user.bodyWeight)
        assert(result?.occupation == user.occupation)
    }


    @Test
    fun `should find user by userID`() {
        val user = User.create(
            userID = "test",
            firstName = "test",
            lastName = "test",
            gender = "male",
            email = "sample@test.com",
            birthDate = LocalDate.of(1990, 1, 1),
            address = "test",
            postalCode = "0000000",
            phoneNumber = "00000000000",
            iconImageUrl = "https://example.com",
            bodyHeight = 170.4,
            bodyWeight = 60.4,
            occupation = "test"
        )

        val foundUser = userRepository.getOrNullBy(user.userID)

        assert(foundUser?.userID == user.userID)
        assert(foundUser?.firstName == user.firstName)
        assert(foundUser?.lastName == user.lastName)
        assert(foundUser?.email == user.email)
        assert(foundUser?.birthDate == user.birthDate)
        assert(foundUser?.address == user.address)
        assert(foundUser?.postalCode == user.postalCode)
        assert(foundUser?.phoneNumber == user.phoneNumber)
        assert(foundUser?.iconImageUrl == user.iconImageUrl)
        assert(foundUser?.bodyHeight == user.bodyHeight)
        assert(foundUser?.bodyWeight == user.bodyWeight)
        assert(foundUser?.occupation == user.occupation)
    }
}