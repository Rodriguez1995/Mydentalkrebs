package idat.edu.pe.mydentalkrebs.model

data class Doctor(val id: Int, val name: String) {
    override fun toString(): String {
        return name
    }
}