package io.smooth.constraint.android

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import io.smooth.constraint.BaseConstraint
import io.smooth.constraint.Constraint
import io.smooth.constraint.ConstraintStatus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class PermissionConstraint(
    private val context: Context,
    private val requiredPermissions: Array<String>,
    private val rationale: String? = null,
    private val options: Permissions.Options? = null
) : Constraint {

    override suspend fun check(): Flow<ConstraintStatus> = flow {
        Permissions.check(
            context,
            requiredPermissions,
            rationale,
            options,
            object : PermissionHandler() {
                override fun onGranted() {
                    val result = hasPermissions(*requiredPermissions)
                    GlobalScope.launch {
                        if (result) emit(ConstraintStatus.CONSTRAINT_MET)
                        else emit(ConstraintStatus.CONSTRAINT_NOT_MET)
                    }
                }
            })

    }

    private fun hasPermissions(
        vararg permissions: String
    ): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

}