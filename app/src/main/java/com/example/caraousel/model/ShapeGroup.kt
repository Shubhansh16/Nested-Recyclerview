package com.example.caraousel.model

import com.example.caraousel.ShapeType

data class ShapeGroup(
    val title:String,
    val shapeType: ShapeType,
    val character: List<Album>
)
