package org.springframework.data.r2dbc.function

inline fun <reified T> DatabaseClient.SelectFromSpec.from() = from(T::class.java)

inline fun <reified T> DatabaseClient.GenericExecuteSpec.mapFirst() = map { r, _ -> r[0] as T }
inline fun <reified T> DatabaseClient.InsertSpec<Map<String, Any>>.mapFirst() = map { r, _ -> r[0] as T }
