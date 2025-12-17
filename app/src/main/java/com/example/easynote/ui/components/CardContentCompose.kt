package com.example.easynote.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easynote.R
import com.example.easynote.models.Note


@Composable
fun InfoImageCard(
    note: Note?,
    onDeleteClick: (Int) -> Unit
) {
    val image: Painter = when (note?.noteTableId) {
        0 -> painterResource(R.drawable.event)
        1 -> painterResource(R.drawable.clock)
        2 -> painterResource(R.drawable.weight)
        3 -> painterResource(R.drawable.spending)
        else -> painterResource(R.drawable.other)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column {

            // Cuadro de la imagen
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            // Contenido de la tarjeta
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = note?.title ?: "",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = note?.originalContent ?: "",
                            fontSize = 14.sp,
                            color = Color(0xFF444444)
                        )
                        Spacer(Modifier.height(4.dp))

                        val dateText = note?.fields
                            ?.get("Fecha")
                            ?.toString()
                            ?: "no especificado"

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF4CAF50), shape = CircleShape)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "Fecha: $dateText",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = note?.timestamp
                                ?.let { extractDate(it) }
                                ?: "",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }

                    //Botón de eliminar
                    IconButton(onClick = {onDeleteClick(note?.id ?: 0)}) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}

