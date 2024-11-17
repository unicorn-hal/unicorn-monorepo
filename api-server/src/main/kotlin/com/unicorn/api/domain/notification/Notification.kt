package com.unicorn.api.domain.notification

import com.unicorn.api.domain.user.UserID

data class Notification private constructor(
    val userID: UserID,
    val isMedicineReminder: IsMedicineReminder,
    val isRegularHealthCheckup: IsRegularHealthCheckup,
    val isHospitalNews: IsHospitalNews,
) {
    companion object {
        fun fromStore(
            userID: String,
            isMedicineReminder: Boolean,
            isRegularHealthCheckup: Boolean,
            isHospitalNews: Boolean,
        ): Notification {
            return Notification(
                userID = UserID(userID),
                isMedicineReminder = IsMedicineReminder(isMedicineReminder),
                isRegularHealthCheckup = IsRegularHealthCheckup(isRegularHealthCheckup),
                isHospitalNews = IsHospitalNews(isHospitalNews),
            )
        }

        fun create(
            userID: String,
            isMedicineReminder: Boolean,
            isRegularHealthCheckup: Boolean,
            isHospitalNews: Boolean,
        ): Notification {
            return Notification(
                userID = UserID(userID),
                isMedicineReminder = IsMedicineReminder(isMedicineReminder),
                isRegularHealthCheckup = IsRegularHealthCheckup(isRegularHealthCheckup),
                isHospitalNews = IsHospitalNews(isHospitalNews),
            )
        }
    }

    fun update(
        isMedicineReminder: IsMedicineReminder,
        isRegularHealthCheckup: IsRegularHealthCheckup,
        isHospitalNews: IsHospitalNews,
    ): Notification {
        return this.copy(
            isMedicineReminder = isMedicineReminder,
            isRegularHealthCheckup = isRegularHealthCheckup,
            isHospitalNews = isHospitalNews,
        )
    }
}

@JvmInline
value class IsMedicineReminder(val value: Boolean)

@JvmInline
value class IsRegularHealthCheckup(val value: Boolean)

@JvmInline
value class IsHospitalNews(val value: Boolean)
