package com.example.repository

import com.example.models.Patients
import com.example.models.Patient
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PatientRepository {

    fun addPatient(rut: String, name: String, email: String?): Int {
        return transaction {
            Patients.insert {
                it[Patients.rut] = rut
                it[Patients.name] = name
                it[Patients.email] = email
            } get Patients.id
        }.value
    }

    fun getAllPatients(): List<Patient> {
        return transaction {
            Patients.selectAll().map {
                Patient(
                    id = it[Patients.id].value,
                    rut = it[Patients.rut],
                    name = it[Patients.name],
                    email = it[Patients.email]
                )
            }
        }
    }

    fun getPatientByRut(rut: String): Patient? {
        return transaction {
            Patients.select { Patients.rut eq rut }
                .map {
                    Patient(
                        id = it[Patients.id].value,
                        rut = it[Patients.rut],
                        name = it[Patients.name],
                        email = it[Patients.email]
                    )
                }.singleOrNull()
        }
    }
}