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
        map.put("abc", SignData("HelloSign.png", "asdf", "Hello"))
        map.put("def", SignData("not_found.png", "asdf", "asdf"))
        map.put("ghi", SignData("ThankYouSign.png", "asdf", "Thank You"))
    }


    override fun getData(): List<SignData> {
        val list = ArrayList<SignData>()
        for ((index, value) in map) {
            list.add(SignData(value.imagePath, value.videoPath, value.word))
        }
        return list;
    }

    override fun getSignDataByText(): SignData {
        return SignData("", "", "")
    }

}

interface SignDataInterface {
    fun getData(): List<SignData>;
    fun getSignDataByText(): SignData;
}