package com.kropiejohn.ftc.glasses.model

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*
import kotlin.collections.HashMap

object GlassesDatabase {
    private val db = HashMap<Int, Glasses>()
    internal lateinit var connection: Connection
    const val tableName = "GlassesDB"

    init {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver")
            connection = DriverManager.getConnection("jdbc:derby:$tableName;create=true")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        restore()
    }

    fun get(id: Int): Glasses? {
        return db.get(id)
    }

    fun createUpdate(glasses: Glasses) {
        db.put(glasses.number, glasses)
        save(listOf(glasses))
    }

    fun getAll(): Collection<Glasses> {
        return db.values
    }

    fun getNewIndex(): Int {
        return db.size + 1
    }

    fun containsKey(key: Int): Boolean {
        return db.containsKey(key)
    }

    fun isEmpty(): Boolean {
        return db.isEmpty()
    }

    private fun restore() {
        try {// Create table to start.
            val sqlCreate = "CREATE TABLE $tableName (ID INTEGER, " +
                    "rightSphere DOUBLE, rightCylinder DOUBLE, rightAxis INTEGER, rightFocal INTEGER, leftSphere DOUBLE, " +
                    "leftCylinder DOUBLE, leftAxis INTEGER, leftFocal INTEGER, entryDate DATE, removed CHAR, " +
                    "sex CHAR, age CHAR)"
            val statement = connection.createStatement()
            statement.executeUpdate(sqlCreate)
        } catch (e: SQLException) {
            // Squash table already exists errors.
        }

        val getGlassesSql = "SELECT * FROM $tableName"
        val statement = connection.createStatement()
        val glassesRs = statement.executeQuery(getGlassesSql)

        while (glassesRs.next()) {
            val newGlasses = Glasses()
            newGlasses.number = glassesRs.getInt("ID")
            newGlasses.rightSphere = glassesRs.getDouble("rightSphere")
            newGlasses.rightCylinder = glassesRs.getDouble("rightCylinder")
            newGlasses.rightAxis = glassesRs.getInt("rightAxis")
            newGlasses.rightFocal = glassesRs.getInt("rightFocal")

            newGlasses.leftSphere = glassesRs.getDouble("leftSphere")
            newGlasses.leftCylinder = glassesRs.getDouble("leftCylinder")
            newGlasses.leftAxis = glassesRs.getInt("leftAxis")
            newGlasses.leftFocal = glassesRs.getInt("leftFocal")

            newGlasses.entryDate = Date(glassesRs.getDate("entryDate").time)
            newGlasses.isRemoved = glassesRs.getString("removed") == YesNo.YES.abbreviation
            newGlasses.gender = Gender.MALE.getEnumFromAbbreviationOrName(glassesRs.getString("sex"))
            newGlasses.age = Age.ADULT.getEnumFromAbbreviationOrName(glassesRs.getString("age"))

            db.put(newGlasses.number, newGlasses)
        }
    }

    private fun save(toSave: Collection<Glasses>) {
        var statement = connection.createStatement()
        val sql = "DELETE FROM $tableName"
        // Execute deletion
        statement.executeUpdate(sql)

        val insertSql = "INSERT INTO $tableName (ID , rightSphere, rightCylinder, rightAxis, rightFocal, " +
                "leftSphere, leftCylinder, leftAxis, leftFocal, entryDate, removed, sex, age) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        val insertStatement = connection.prepareStatement(insertSql)
        connection.autoCommit = false
        toSave.forEach {
            var counter = 0
            insertStatement.setInt(++counter, it.number)
            insertStatement.setDouble(++counter, it.getRightSphere())
            insertStatement.setDouble(++counter, it.getRightCylinder())
            insertStatement.setInt(++counter, it.getRightAxis())
            insertStatement.setInt(++counter, it.getRightFocal())
            insertStatement.setDouble(++counter, it.getLeftSphere())
            insertStatement.setDouble(++counter, it.getLeftCylinder())
            insertStatement.setInt(++counter, it.getLeftAxis())
            insertStatement.setInt(++counter, it.getLeftFocal())
            insertStatement.setDate(++counter, java.sql.Date(it.entryDate.time))
            insertStatement.setString(++counter, YesNo.fromBoolean(it.isRemoved).abbreviation)
            insertStatement.setString(++counter, it.gender.abbreviation)
            insertStatement.setString(++counter, it.age.abbreviation)
            insertStatement.addBatch()
        }

        insertStatement.executeBatch()
        connection.commit()
    }

    fun replaceContents(glasses: List<Glasses>) {
        db.clear()
        glasses.forEach { db.put(it.number, it) }
        save(db.values)
    }
}