package org.sharad.velvetinvestment.features.onboarding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController

@Stable
class FormFocusController internal constructor(
    private val focusManager: FocusManager,
    private val keyboardController: SoftwareKeyboardController?
) {
    private var highlighted: Any? by mutableStateOf(null)

    fun isHighlighted(key: Any): Boolean = highlighted == key

    fun clearHighlight() {
        highlighted = null
    }

    fun focus(requester: FocusRequester) {
        highlighted = null
        if (!requester.requestFocus()) focusManager.moveFocus(FocusDirection.Next)
    }

    fun highlight(key: Any) {
        dismissKeyboard()
        highlighted = key
    }

    fun dismissKeyboard() {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    fun submit(canSubmit: Boolean, onSubmit: () -> Unit) {
        dismissKeyboard()
        highlighted = null
        if (canSubmit) onSubmit()
    }
}

@Composable
fun rememberFormFocusController(): FormFocusController {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    return remember(focusManager, keyboardController) {
        FormFocusController(focusManager, keyboardController)
    }
}

fun Modifier.formField(
    controller: FormFocusController,
    requester: FocusRequester? = null
): Modifier = (if (requester != null) focusRequester(requester) else this)
    .onFocusChanged { if (it.hasFocus) controller.clearHighlight() }
