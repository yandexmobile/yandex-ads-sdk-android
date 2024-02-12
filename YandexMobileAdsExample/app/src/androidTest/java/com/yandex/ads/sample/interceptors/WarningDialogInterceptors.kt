package com.yandex.ads.sample.interceptors

import androidx.test.espresso.DataInteraction
import androidx.test.espresso.ViewInteraction
import com.kaspersky.components.kautomator.intercept.interaction.UiDeviceInteraction
import com.kaspersky.components.kautomator.intercept.interaction.UiObjectInteraction
import com.kaspersky.components.kautomator.intercept.operation.UiDeviceAction
import com.kaspersky.components.kautomator.intercept.operation.UiDeviceAssertion
import com.kaspersky.components.kautomator.intercept.operation.UiObjectAction
import com.kaspersky.components.kautomator.intercept.operation.UiObjectAssertion
import com.kaspersky.kaspresso.instrumental.InstrumentalDependencyProvider
import com.kaspersky.kaspresso.interceptors.behavior.DataBehaviorInterceptor
import com.kaspersky.kaspresso.interceptors.behavior.ViewBehaviorInterceptor
import com.kaspersky.kaspresso.interceptors.behaviorkautomator.DeviceBehaviorInterceptor
import com.kaspersky.kaspresso.interceptors.behaviorkautomator.ObjectBehaviorInterceptor
import com.kaspersky.kaspresso.logger.UiTestLogger

internal class WarningDialogSafetyDeviceBehaviorInterceptor(
    private val delegate: WarningDialogSuppressor
) : DeviceBehaviorInterceptor {

    constructor(
        logger: UiTestLogger,
        instrumentalDependencyProvider: InstrumentalDependencyProvider
    ) : this(DefaultWarningDialogSuppressor(logger, instrumentalDependencyProvider))

    override fun <T> interceptCheck(
        interaction: UiDeviceInteraction,
        assertion: UiDeviceAssertion,
        activity: () -> T
    ): T = delegate.suppressWarningDialog(activity)

    override fun <T> interceptPerform(
        interaction: UiDeviceInteraction,
        action: UiDeviceAction,
        activity: () -> T
    ): T = delegate.suppressWarningDialog(activity)
}

internal class WarningDialogSafetyObjectBehaviorInterceptor(
    private val delegate: WarningDialogSuppressor
) : ObjectBehaviorInterceptor {

    constructor(
        logger: UiTestLogger,
        instrumentalDependencyProvider: InstrumentalDependencyProvider
    ) : this(DefaultWarningDialogSuppressor(logger, instrumentalDependencyProvider))

    override fun <T> interceptCheck(
        interaction: UiObjectInteraction,
        assertion: UiObjectAssertion, activity: () -> T
    ): T = delegate.suppressWarningDialog(activity)

    override fun <T> interceptPerform(
        interaction: UiObjectInteraction,
        action: UiObjectAction, activity: () -> T
    ): T = delegate.suppressWarningDialog(activity)

}

internal class WarningDialogSafetyDataBehaviorInterceptor(
    private val delegate: WarningDialogSuppressor
) : DataBehaviorInterceptor {

    constructor(
        logger: UiTestLogger,
        instrumentalDependencyProvider: InstrumentalDependencyProvider
    ) : this(DefaultWarningDialogSuppressor(logger, instrumentalDependencyProvider))

    override fun <T> intercept(
        interaction: DataInteraction,
        action: () -> T
    ): T = delegate.suppressWarningDialog(action)
}

internal class WarningDialogSafetyViewBehaviorInterceptor(
    private val delegate: WarningDialogSuppressor
) : ViewBehaviorInterceptor {

    constructor(
        logger: UiTestLogger,
        instrumentalDependencyProvider: InstrumentalDependencyProvider
    ) : this(DefaultWarningDialogSuppressor(logger, instrumentalDependencyProvider))

    override fun <T> intercept(
        interaction: ViewInteraction,
        action: () -> T
    ): T = delegate.suppressWarningDialog(action)
}
