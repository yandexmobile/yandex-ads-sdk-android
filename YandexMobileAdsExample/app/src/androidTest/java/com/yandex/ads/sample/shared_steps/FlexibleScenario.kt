package com.yandex.ads.sample.shared_steps

import com.kaspersky.kaspresso.testcases.api.scenario.BaseScenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

internal fun <ScenarioData> scenarioOf(block: TestContext<ScenarioData>.() -> Unit) = FlexibleScenario(block)

internal class FlexibleScenario<ScenarioData>(
    override val steps: TestContext<ScenarioData>.() -> Unit
) : BaseScenario<ScenarioData>()
