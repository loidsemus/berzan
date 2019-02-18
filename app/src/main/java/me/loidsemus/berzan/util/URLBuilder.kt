package me.loidsemus.berzan.util

class URLBuilder(val week: Int, val day: Int, val schoolClass: String, val resolution: Resolution) {

    override fun toString(): String {
        return "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=89920/sv-se&type=1&id=${schoolClass}&period=&week=$week&mode=0&printer=0&colors=32&head=0&clock=0&foot=0&day=$day&width=${resolution.width}&height=${resolution.height}"
    }

    class Resolution(val width: Int, val height: Int)

}