package com.github.takahirom.roborazzi.sample

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziOptions.AccessibilityChecker
import com.github.takahirom.roborazzi.RoborazziOptions.AccessibilityChecks
import com.github.takahirom.roborazzi.RoborazziOptions.RecordOptions
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.RoborazziRule.CaptureType
import com.github.takahirom.roborazzi.RoborazziRule.Options
import com.github.takahirom.roborazzi.atf
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckPreset
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityCheckResult.AccessibilityCheckResultType
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel4, sdk = [35])
class ComposeA11yTest {
  @Suppress("DEPRECATION")
  @get:Rule(order = Int.MIN_VALUE)
  var thrown: ExpectedException = ExpectedException.none()

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @get:Rule
  val roborazziRule = RoborazziRule(
    composeRule = composeTestRule, captureRoot = composeTestRule.onRoot(), options = Options(
      captureType = CaptureType.LastImage(), roborazziOptions = RoborazziOptions(
        recordOptions = RecordOptions(
          applyDeviceCrop = true, accessibilityChecks = AccessibilityChecks.Validate(
            checker = AccessibilityChecker.atf(preset = AccessibilityCheckPreset.LATEST) {
              setThrowExceptionFor(AccessibilityCheckResultType.WARNING)
            })
        ),
      )
    )
  )

  @Test
  fun clickableWithoutSemantics() {
    thrown.expectMessage("SpeakableTextPresentCheck")

    composeTestRule.setContent {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(Modifier.size(48.dp).background(Color.Black).clickable {})
      }
    }
  }

  @Test
  fun boxWithEmptyContentDescription() {
    thrown.expectMessage("SpeakableTextPresentCheck")

    composeTestRule.setContent {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(Modifier.size(48.dp).background(Color.Black).semantics {
          contentDescription = ""
        })
      }
    }
  }

  @Test
  fun smallClickable() {
    // TODO check why not failing
//    thrown.expectMessage("sdffsd")

    composeTestRule.setContent {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {}, modifier = Modifier.size(30.dp)) {
          Text("Something to Click")
        }
      }
    }
  }

  @Test
  fun clickableBox() {
    composeTestRule.setContent {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {}) {
          Text("Something to Click")
        }
      }
    }
  }
}

