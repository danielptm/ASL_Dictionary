package com.gatech.asl_dictionary

class SignData(imagePath: String, videoPath: String, word: String) {
    var imagePath = "not_found.png";
    var videoPath = "";
    var word = "";
    init {
        this.imagePath = imagePath;
        this.videoPath = videoPath;
        this.word = word;
    }
}

class SignDataLocalStore : SignDataInterface  {
    var map: HashMap<String, SignData> = HashMap<String, SignData> ()

    init {
        map.put("Hello", SignData("HelloSign.png", "asdf", "Hello"))
        map.put("Thank You", SignData("ThankYouSign.png", "asdf", "Thank You"))
        map.put("I", SignData("asdf", "asdf", "I"))
        map.put("Me", SignData("asdf", "asdf", "Me"))
        map.put("You", SignData("asdf", "asdf", "You"))
        map.put("He", SignData("asdf", "asdf", "He"))
        map.put("She", SignData("asdf", "asdf", "She"))
        map.put("We", SignData("asdf", "asdf", "We"))
        map.put("Us", SignData("asdf", "asdf", "Us"))
        map.put("It", SignData("asdf", "asdf", "It"))
        map.put("In", SignData("asdf", "asdf", "In"))
        map.put("On", SignData("asdf", "asdf", "On"))
        map.put("To", SignData("asdf", "asdf", "To"))
        map.put("At", SignData("asdf", "asdf", "At"))
        map.put("Be", SignData("asdf", "asdf", "Be"))
        map.put("My", SignData("asdf", "asdf", "My"))
        map.put("Up", SignData("asdf", "asdf", "Up"))
        map.put("Go", SignData("asdf", "asdf", "Go"))
        map.put("Am", SignData("asdf", "asdf", "Am"))
        map.put("Do", SignData("asdf", "asdf", "Do"))
        map.put("Is", SignData("asdf", "asdf", "Is"))
        map.put("So", SignData("asdf", "asdf", "So"))
        map.put("If", SignData("asdf", "asdf", "If"))
        map.put("As", SignData("asdf", "asdf", "As"))
        map.put("An", SignData("asdf", "asdf", "An"))
    }


    override fun getData(): List<SignData> {
        val list = ArrayList<SignData>()
        for ((index, value) in map) {
            list.add(SignData(value.imagePath, value.videoPath, value.word))
        }
        return list;
    }

    override fun getSignDataByText(word: String): SignData {
        return map.getOrElse(word, {SignData("", "", "")});
    }

}

interface SignDataInterface {
    fun getData(): List<SignData>;
    fun getSignDataByText(word: String): SignData;
}