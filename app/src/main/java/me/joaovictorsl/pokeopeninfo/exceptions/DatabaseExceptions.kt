package me.joaovictorsl.pokeopeninfo.exceptions

class DatabaseNotInitializedException(
    msg: String = "Database is not initialized. Make sure you call DatabaseFacade.initialize() before getting an instance."
) : Exception(msg)
