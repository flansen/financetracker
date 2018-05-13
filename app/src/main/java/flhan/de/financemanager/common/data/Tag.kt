package flhan.de.financemanager.common.data

data class Tag(
        val name: String = "",
        var id: String = "",
        var numberOfOccurence: Int = 0
) {
    companion object {
        const val COUNT = "numberOfOccurence"
    }
}