package com.aquent.reactive

import org.springframework.data.r2dbc.function.*

data class Person(
        var personId: Int?,
        var firstName: String,
        var lastName: String
)

// language=PostgreSQL
const val INIT_SQL = """
CREATE TABLE IF NOT EXISTS person (
    person_id  serial primary key not null,
    first_name text not null,
    last_name  text not null
);
"""

class PersonRepository(private val client: DatabaseClient) {

    fun count() =
            client.execute().sql("SELECT COUNT(*) FROM person")
                    .mapFirst<Long>().one()

    fun get(id: Int) =
            client.execute().sql("SELECT * FROM person WHERE person_id = \$1")
                    .bind(0, id).asType<Person>().fetch().first()

    fun insert(person: Person) =
            client.insert().into<Person>().using(person)
                    .mapFirst<Int>().one()
                    .flatMap { get(it.toInt()) }

    fun update(person: Person) =
            // no client.update() yet?
            client.execute().sql("UPDATE person SET first_name = \$1, last_name = \$2 WHERE person_id = \$3")
                    .bind(0, person.firstName).bind(1, person.lastName).bind(2, person.personId!!)
                    .then().flatMap { get(person.personId!!) }

    fun delete(id: Int) =
            client.execute().sql("DELETE FROM person WHERE person_id = \$1")
                    .bind(0, id).fetch().rowsUpdated()

    fun getAll() = client.select().from<Person>().fetch().all()

    fun init() = client.execute().sql(INIT_SQL).then()

}

