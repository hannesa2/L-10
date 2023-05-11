package app.k9mail.feature.account.setup.ui.options

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import app.k9mail.core.ui.compose.common.DevicePreviews
import app.k9mail.core.ui.compose.designsystem.atom.text.TextOverline
import app.k9mail.core.ui.compose.designsystem.molecule.input.SelectInput
import app.k9mail.core.ui.compose.designsystem.molecule.input.SwitchInput
import app.k9mail.core.ui.compose.designsystem.molecule.input.TextInput
import app.k9mail.core.ui.compose.designsystem.template.ResponsiveWidthContainer
import app.k9mail.core.ui.compose.theme.K9Theme
import app.k9mail.core.ui.compose.theme.MainTheme
import app.k9mail.core.ui.compose.theme.ThunderbirdTheme
import app.k9mail.feature.account.setup.R
import app.k9mail.feature.account.setup.domain.entity.EmailCheckFrequency
import app.k9mail.feature.account.setup.domain.entity.EmailDisplayCount
import app.k9mail.feature.account.setup.ui.common.defaultHeadlineItemPadding
import app.k9mail.feature.account.setup.ui.common.defaultItemPadding
import app.k9mail.feature.account.setup.ui.options.AccountOptionsContract.Event
import app.k9mail.feature.account.setup.ui.options.AccountOptionsContract.State

@OptIn(ExperimentalLayoutApi::class)
@Suppress("LongMethod")
@Composable
internal fun AccountOptionsContent(
    state: State,
    onEvent: (Event) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val resources = LocalContext.current.resources

    ResponsiveWidthContainer(
        modifier = Modifier
            .testTag("AccountOptionsContent")
            .consumeWindowInsets(contentPadding)
            .padding(contentPadding)
            .then(modifier),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(MainTheme.spacings.default),
        ) {
            item {
                TextOverline(
                    text = stringResource(id = R.string.account_setup_options_section_display_options),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(defaultHeadlineItemPadding()),
                )
            }

            item {
                TextInput(
                    text = state.accountName.value,
                    onTextChange = { onEvent(Event.OnAccountNameChanged(it)) },
                    label = stringResource(id = R.string.account_setup_options_account_name_label),
                    contentPadding = defaultItemPadding(),
                )
            }

            item {
                TextInput(
                    text = state.displayName.value,
                    onTextChange = { onEvent(Event.OnDisplayNameChanged(it)) },
                    label = stringResource(id = R.string.account_setup_options_display_name_label),
                    contentPadding = defaultItemPadding(),
                    isRequired = true,
                )
            }

            item {
                TextInput(
                    text = state.emailSignature.value,
                    onTextChange = { onEvent(Event.OnEmailSignatureChanged(it)) },
                    label = stringResource(id = R.string.account_setup_options_email_signature_label),
                    contentPadding = defaultItemPadding(),
                    isSingleLine = false,
                )
            }

            item {
                TextOverline(
                    text = stringResource(id = R.string.account_setup_options_section_sync_options_),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(defaultHeadlineItemPadding()),
                )
            }

            item {
                SelectInput(
                    options = EmailCheckFrequency.all(),
                    optionToStringTransformation = { transformCheckFrequency(resources, it) },
                    selectedOption = state.checkFrequency,
                    onOptionChange = { onEvent(Event.OnCheckFrequencyChanged(it)) },
                    label = stringResource(id = R.string.account_setup_options_account_check_frequency_label),
                    contentPadding = defaultItemPadding(),
                )
            }

            item {
                SelectInput(
                    options = EmailDisplayCount.all(),
                    optionToStringTransformation = { transformMessageDisplayCount(resources, it) },
                    selectedOption = state.messageDisplayCount,
                    onOptionChange = { onEvent(Event.OnMessageDisplayCountChanged(it)) },
                    label = stringResource(id = R.string.account_setup_options_email_display_count_label),
                    contentPadding = defaultItemPadding(),
                )
            }

            item {
                SwitchInput(
                    text = stringResource(id = R.string.account_setup_options_show_notifications_label),
                    checked = state.showNotification,
                    onCheckedChange = { onEvent(Event.OnShowNotificationChanged(it)) },
                    contentPadding = defaultItemPadding(),
                )
            }

            item {
                Spacer(modifier = Modifier.requiredHeight(MainTheme.sizes.smaller))
            }
        }
    }
}

@Suppress("MagicNumber")
private fun transformCheckFrequency(
    resources: Resources,
    it: EmailCheckFrequency,
): String {
    return when (it.minutes) {
        -1 -> resources.getString(R.string.account_setup_options_email_check_frequency_zero)

        in 1..59 -> resources.getQuantityString(
            R.plurals.account_setup_options_email_check_frequency_minutes,
            it.minutes,
            it.minutes,
        )

        else -> resources.getQuantityString(
            R.plurals.account_setup_options_email_check_frequency_hours,
            (it.minutes / 60),
            (it.minutes / 60),
        )
    }
}

private fun transformMessageDisplayCount(
    resources: Resources,
    it: EmailDisplayCount,
) = resources.getQuantityString(
    R.plurals.account_setup_options_email_display_count_messages,
    it.count,
    it.count,
)

@Composable
@DevicePreviews
internal fun AccountOptionsContentK9Preview() {
    K9Theme {
        AccountOptionsContent(
            state = State(),
            onEvent = {},
            contentPadding = PaddingValues(),
        )
    }
}

@Composable
@DevicePreviews
internal fun AccountOptionsContentThunderbirdPreview() {
    ThunderbirdTheme {
        AccountOptionsContent(
            state = State(),
            onEvent = {},
            contentPadding = PaddingValues(),
        )
    }
}
