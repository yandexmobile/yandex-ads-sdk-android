package com.yandex.ads.sample.interceptors

import com.kaspersky.kaspresso.kaspresso.Kaspresso

internal fun Kaspresso.Builder.addBehaviorInterceptors() {
    val notRespondingDeviceInterceptor = WarningDialogSafetyDeviceBehaviorInterceptor(
        logger = libLogger,
        instrumentalDependencyProvider = instrumentalDependencyProviderFactory
            .getInterceptorProvider<WarningDialogSafetyDeviceBehaviorInterceptor>(instrumentation)
    )

    val notRespondingObjectInterceptor = WarningDialogSafetyObjectBehaviorInterceptor(
        logger = libLogger,
        instrumentalDependencyProvider = instrumentalDependencyProviderFactory
            .getInterceptorProvider<WarningDialogSafetyObjectBehaviorInterceptor>(instrumentation)
    )

    val notRespondingDataInterceptor = WarningDialogSafetyDataBehaviorInterceptor(
        logger = libLogger,
        instrumentalDependencyProvider = instrumentalDependencyProviderFactory
            .getInterceptorProvider<WarningDialogSafetyDataBehaviorInterceptor>(instrumentation)
    )

    val notRespondingViewInterceptor = WarningDialogSafetyViewBehaviorInterceptor(
        logger = libLogger,
        instrumentalDependencyProvider = instrumentalDependencyProviderFactory
            .getInterceptorProvider<WarningDialogSafetyViewBehaviorInterceptor>(instrumentation)
    )

    deviceBehaviorInterceptors.add(0, notRespondingDeviceInterceptor)
    objectBehaviorInterceptors.add(0, notRespondingObjectInterceptor)
    dataBehaviorInterceptors.add(0, notRespondingDataInterceptor)
    viewBehaviorInterceptors.add(0, notRespondingViewInterceptor)
}
