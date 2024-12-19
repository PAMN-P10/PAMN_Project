package com.pamn.letscook.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamn.letscook.R


@Composable
fun FooterNavigation(
    onHeartClick: () -> Unit,
    onAddClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier

) {
    androidx.compose.material3.Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        color = androidx.compose.material3.MaterialTheme.colorScheme.background,
        shadowElevation = 4.dp,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(30)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FooterButton(iconRes = R.drawable.heart, onClick = onHeartClick)
            FooterButton(iconRes = R.drawable.plus, onClick = onAddClick)
            FooterButton(iconRes = R.drawable.user, onClick = onProfileClick)
        }
    }
}

@Composable
fun FooterButton(
    iconRes: Int,
    onClick: () -> Unit
) {
    androidx.compose.material3.IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(35.dp)
            .background(
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
        )
    }
}
