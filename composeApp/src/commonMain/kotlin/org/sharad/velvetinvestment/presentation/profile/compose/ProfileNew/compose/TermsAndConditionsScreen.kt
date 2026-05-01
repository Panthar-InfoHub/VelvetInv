package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.tc_account_item_1
import velvet.composeapp.generated.resources.tc_account_item_2
import velvet.composeapp.generated.resources.tc_account_item_3
import velvet.composeapp.generated.resources.tc_licence_item_1
import velvet.composeapp.generated.resources.tc_licence_item_2
import velvet.composeapp.generated.resources.tc_licence_item_3
import velvet.composeapp.generated.resources.tc_prohibited_item_1
import velvet.composeapp.generated.resources.tc_prohibited_item_2
import velvet.composeapp.generated.resources.tc_prohibited_item_3
import velvet.composeapp.generated.resources.tc_prohibited_item_4
import velvet.composeapp.generated.resources.tc_prohibited_item_5
import velvet.composeapp.generated.resources.tc_prohibited_item_6
import velvet.composeapp.generated.resources.tc_section_1_content
import velvet.composeapp.generated.resources.tc_section_1_title
import velvet.composeapp.generated.resources.tc_section_12_content
import velvet.composeapp.generated.resources.tc_section_12_title
import velvet.composeapp.generated.resources.tc_section_2_footer
import velvet.composeapp.generated.resources.tc_section_2_intro
import velvet.composeapp.generated.resources.tc_section_2_title
import velvet.composeapp.generated.resources.tc_section_3_intro
import velvet.composeapp.generated.resources.tc_section_3_title
import velvet.composeapp.generated.resources.tc_section_4_content
import velvet.composeapp.generated.resources.tc_section_4_title
import velvet.composeapp.generated.resources.tc_section_5_footer
import velvet.composeapp.generated.resources.tc_section_5_intro
import velvet.composeapp.generated.resources.tc_section_5_title
import velvet.composeapp.generated.resources.tc_section_6_intro
import velvet.composeapp.generated.resources.tc_section_6_title
import velvet.composeapp.generated.resources.tc_user_content_item_1
import velvet.composeapp.generated.resources.tc_user_content_item_2
import velvet.composeapp.generated.resources.tc_user_content_item_3
import velvet.composeapp.generated.resources.terms_conditions_header
import velvet.composeapp.generated.resources.terms_conditions_intro
import velvet.composeapp.generated.resources.terms_conditions_title
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TermsAndConditionsScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        BackHeader(
            heading = stringResource(Res.string.terms_conditions_title),
            showBack = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(Res.string.terms_conditions_header),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.terms_conditions_intro),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.tc_section_1_title))
                Text(
                    text = stringResource(Res.string.tc_section_1_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.tc_section_2_title))
                Text(
                    text = stringResource(Res.string.tc_section_2_intro),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val items = listOf(
                    Res.string.tc_account_item_1, Res.string.tc_account_item_2,
                    Res.string.tc_account_item_3
                )
                items.forEach { BulletItem(stringResource(it)) }
                Text(
                    text = stringResource(Res.string.tc_section_2_footer),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.tc_section_3_title))
                Text(
                    text = stringResource(Res.string.tc_section_3_intro),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val items = listOf(
                    Res.string.tc_licence_item_1, Res.string.tc_licence_item_2,
                    Res.string.tc_licence_item_3
                )
                items.forEach { BulletItem(stringResource(it)) }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.tc_section_4_title))
                Text(
                    text = stringResource(Res.string.tc_section_4_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.tc_section_5_title))
                Text(
                    text = stringResource(Res.string.tc_section_5_intro),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val items = listOf(
                    Res.string.tc_user_content_item_1, Res.string.tc_user_content_item_2,
                    Res.string.tc_user_content_item_3
                )
                items.forEach { BulletItem(stringResource(it)) }
                Text(
                    text = stringResource(Res.string.tc_section_5_footer),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.tc_section_6_title))
                Text(
                    text = stringResource(Res.string.tc_section_6_intro),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val items = listOf(
                    Res.string.tc_prohibited_item_1, Res.string.tc_prohibited_item_2,
                    Res.string.tc_prohibited_item_3, Res.string.tc_prohibited_item_4,
                    Res.string.tc_prohibited_item_5, Res.string.tc_prohibited_item_6
                )
                items.forEach { BulletItem(stringResource(it)) }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.tc_section_12_title))
                Text(
                    text = stringResource(Res.string.tc_section_12_content),
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private @Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = Poppins,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

private @Composable
fun BulletItem(text: String) {
    Text(
        text = "• $text",
        fontSize = 14.sp,
        fontFamily = Poppins,
        color = Color.Gray,
        lineHeight = 20.sp,
        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
    )
}

@Preview
@Composable
fun TermsAndConditionsScreenPreview() {
    VelvetTheme {
        TermsAndConditionsScreen()
    }
}
