package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.pp_contact_email
import velvet.composeapp.generated.resources.pp_contact_email_label
import velvet.composeapp.generated.resources.pp_contact_name
import velvet.composeapp.generated.resources.pp_contact_office
import velvet.composeapp.generated.resources.pp_contact_office_label
import velvet.composeapp.generated.resources.pp_contact_phone
import velvet.composeapp.generated.resources.pp_contact_phone_label
import velvet.composeapp.generated.resources.pp_section_1_a_content
import velvet.composeapp.generated.resources.pp_section_1_a_title
import velvet.composeapp.generated.resources.pp_section_1_b_content
import velvet.composeapp.generated.resources.pp_section_1_b_title
import velvet.composeapp.generated.resources.pp_section_1_c_content
import velvet.composeapp.generated.resources.pp_section_1_c_title
import velvet.composeapp.generated.resources.pp_section_1_title
import velvet.composeapp.generated.resources.pp_section_10_content
import velvet.composeapp.generated.resources.pp_section_10_title
import velvet.composeapp.generated.resources.pp_section_11_content
import velvet.composeapp.generated.resources.pp_section_11_title
import velvet.composeapp.generated.resources.pp_section_12_title
import velvet.composeapp.generated.resources.pp_section_2_intro
import velvet.composeapp.generated.resources.pp_section_2_title
import velvet.composeapp.generated.resources.pp_section_3_content
import velvet.composeapp.generated.resources.pp_section_3_title
import velvet.composeapp.generated.resources.pp_section_4_intro
import velvet.composeapp.generated.resources.pp_section_4_title
import velvet.composeapp.generated.resources.pp_section_5_content
import velvet.composeapp.generated.resources.pp_section_5_title
import velvet.composeapp.generated.resources.pp_section_6_content
import velvet.composeapp.generated.resources.pp_section_6_title
import velvet.composeapp.generated.resources.pp_section_7_content
import velvet.composeapp.generated.resources.pp_section_7_title
import velvet.composeapp.generated.resources.pp_section_8_content
import velvet.composeapp.generated.resources.pp_section_8_title
import velvet.composeapp.generated.resources.pp_section_9_content
import velvet.composeapp.generated.resources.pp_section_9_title
import velvet.composeapp.generated.resources.pp_share_item_1
import velvet.composeapp.generated.resources.pp_share_item_2
import velvet.composeapp.generated.resources.pp_share_item_3
import velvet.composeapp.generated.resources.pp_share_item_4
import velvet.composeapp.generated.resources.pp_share_item_5
import velvet.composeapp.generated.resources.pp_use_item_1
import velvet.composeapp.generated.resources.pp_use_item_2
import velvet.composeapp.generated.resources.pp_use_item_3
import velvet.composeapp.generated.resources.pp_use_item_4
import velvet.composeapp.generated.resources.pp_use_item_5
import velvet.composeapp.generated.resources.pp_use_item_6
import velvet.composeapp.generated.resources.pp_use_item_7
import velvet.composeapp.generated.resources.pp_use_item_8
import velvet.composeapp.generated.resources.privacy_policy_consent
import velvet.composeapp.generated.resources.privacy_policy_header
import velvet.composeapp.generated.resources.privacy_policy_intro
import velvet.composeapp.generated.resources.privacy_policy_last_updated
import velvet.composeapp.generated.resources.privacy_policy_title
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        BackHeader(
            heading = stringResource(Res.string.privacy_policy_title),
            showBack = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(Res.string.privacy_policy_header),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.privacy_policy_intro),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(Res.string.privacy_policy_consent),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.privacy_policy_last_updated),
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_1_title))
                SubSection(
                    title = stringResource(Res.string.pp_section_1_a_title),
                    content = stringResource(Res.string.pp_section_1_a_content)
                )
                SubSection(
                    title = stringResource(Res.string.pp_section_1_b_title),
                    content = stringResource(Res.string.pp_section_1_b_content)
                )
                SubSection(
                    title = stringResource(Res.string.pp_section_1_c_title),
                    content = stringResource(Res.string.pp_section_1_c_content)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_2_title))
                Text(
                    text = stringResource(Res.string.pp_section_2_intro),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val items = listOf(
                    Res.string.pp_use_item_1, Res.string.pp_use_item_2,
                    Res.string.pp_use_item_3, Res.string.pp_use_item_4,
                    Res.string.pp_use_item_5, Res.string.pp_use_item_6,
                    Res.string.pp_use_item_7, Res.string.pp_use_item_8
                )
                items.forEach { BulletItem(stringResource(it)) }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_3_title))
                Text(
                    text = stringResource(Res.string.pp_section_3_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_4_title))
                Text(
                    text = stringResource(Res.string.pp_section_4_intro),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val shareItems = listOf(
                    Res.string.pp_share_item_1, Res.string.pp_share_item_2,
                    Res.string.pp_share_item_3, Res.string.pp_share_item_4,
                    Res.string.pp_share_item_5
                )
                shareItems.forEach { BulletItem(stringResource(it)) }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_5_title))
                Text(
                    text = stringResource(Res.string.pp_section_5_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_6_title))
                Text(
                    text = stringResource(Res.string.pp_section_6_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_7_title))
                Text(
                    text = stringResource(Res.string.pp_section_7_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_8_title))
                Text(
                    text = stringResource(Res.string.pp_section_8_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_9_title))
                Text(
                    text = stringResource(Res.string.pp_section_9_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_10_title))
                Text(
                    text = stringResource(Res.string.pp_section_10_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_11_title))
                Text(
                    text = stringResource(Res.string.pp_section_11_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_12_title))
                Text(
                    text = stringResource(Res.string.pp_contact_name),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                ContactItem(
                    label = stringResource(Res.string.pp_contact_email_label),
                    value = stringResource(Res.string.pp_contact_email)
                )
                ContactItem(
                    label = stringResource(Res.string.pp_contact_phone_label),
                    value = stringResource(Res.string.pp_contact_phone)
                )
                ContactItem(
                    label = stringResource(Res.string.pp_contact_office_label),
                    value = stringResource(Res.string.pp_contact_office)
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Poppins,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun SubSection(title: String, content: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Gray)) {
                append(title)
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.Gray)) {
                append(content)
            }
        },
        fontSize = 14.sp,
        fontFamily = Poppins,
        lineHeight = 20.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun BulletItem(text: String) {
    Row(modifier = Modifier.padding(bottom = 6.dp, start = 8.dp)) {
        Text(
            text = "•",
            fontSize = 14.sp,
            fontFamily = Poppins,
            color = Color.Gray,
            modifier = Modifier.width(16.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = Poppins,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun ContactItem(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp, start = 16.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Poppins,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = Poppins,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    }
}

@Preview
@Composable
fun PrivacyPolicyScreenPreview() {
    VelvetTheme {
        PrivacyPolicyScreen()
    }
}
