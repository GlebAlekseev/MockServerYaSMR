package com.example.domain.entity

data class TodoSync (
    val id: String,
    // Ревизия данных, нужна для синхронизации
    // Является инкрементируемым кол-вом действий
    val dataRevision: Long,
)