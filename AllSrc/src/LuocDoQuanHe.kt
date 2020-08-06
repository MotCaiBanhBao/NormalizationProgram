class LuocDoQuanHe(leftList: MutableList<String>, u: String, rightList: MutableList<String>) {
    val u: String = u
    val left = leftList
    val right = rightList
    var functionalDependency = mutableListOf<MutableList<String>>(leftList, rightList)

    private fun findClosure(source:String, fDs: MutableList<MutableList<String>>):String{
        var isStillChangeAble = true
        var closure = source
        while (isStillChangeAble){
            isStillChangeAble = false;
            for(index in 0 until fDs[0].size)
                if(isContains(closure, fDs[0][index])){
                    val addAttribute = fDs[1][index]
                    if(!isContains(closure, addAttribute)){
                        closure = addAttribute(closure, addAttribute)
                        isStillChangeAble = true
                    }
                }
        }
        return closure
    }
     private fun addAttribute(source: String, attribute: String): String{
        var result = source
        var attributeArray = attribute.toCharArray()
        for(oneAttribute in attributeArray)
            if(!result.contains(oneAttribute))
                result += oneAttribute
        return result
    }
     private fun isContains(Container: String, needCheckString: String): Boolean{
        val needCheckToArray = needCheckString.toCharArray()
        for(temp in needCheckToArray)
            if(!Container.contains(temp))
                return false
        return true
    }
    fun findMinimalCover(){
        var sizeOfMap = functionalDependency[0].size
        var temp = 0
        while (temp<sizeOfMap){
            if(functionalDependency[1][temp].length>1){
                convertRHS(temp)
                temp-=1
                sizeOfMap = functionalDependency[0].size
            }
            temp++
        }
        removeDuplicate()
    }
    private fun convertRHS(index: Int){
        for(temp in (functionalDependency[1][index]).toCharArray()){
            functionalDependency[0].add(functionalDependency[0][index])
            functionalDependency[1].add(temp.toString())
        }
        functionalDependency[0].removeAt(index)
        functionalDependency[1].removeAt(index)
    }
    private fun removeDuplicate(){
        var sizeOfSet = functionalDependency[0].size
        for (temp in 0 until sizeOfSet-2){
            for(temp2 in temp+1 until sizeOfSet-1){
                if(functionalDependency[0][temp] == functionalDependency[0][temp2]
                        && functionalDependency[1][temp] == functionalDependency[1][temp2]){
                    functionalDependency[0].removeAt(temp2)
                    functionalDependency[1].removeAt(temp2)
                    sizeOfSet = functionalDependency[0].size
                }
            }
        }
    }
    fun findLHSExtraneous(){
        var sizeOfSet = functionalDependency[0].size
        for(temp in 0 until sizeOfSet){
            var subString = findAllSubString(functionalDependency[0][temp])
            for (temp2 in subString){
                if(isContains(findClosure(temp2, functionalDependency), functionalDependency[1][temp])){
                    functionalDependency[0][temp] = temp2
                    break
                }
            }
        }
    }
    private fun findAllSubString(source: String): MutableList<String>{
        var sourceArray = source.toCharArray()
        val arrSize = source.length
        var result: MutableList<String> = mutableListOf()
        var tempString: String = ""
        for (startPoint in 0 until arrSize) {
            for (temp in startPoint until arrSize) {
                for (temp2 in startPoint .. temp){
                    tempString += sourceArray[temp2]
                }
                result.add(tempString)
                tempString = ""
            }
        }
        for(i in 0 until result.size-1){
            if(result[i].length==arrSize)
                result.removeAt(i)
        }
        return result
    }
    fun removeRedundantFunctional(){
        var size = functionalDependency.size
        for (index in 0 until size){
            var temp = removeFD(functionalDependency, index)
            println(functionalDependency[0][index])
            print(findClosure(functionalDependency[0][index], temp))
            if (isContains(findClosure(functionalDependency[0][index], temp), functionalDependency[1][index])){
                functionalDependency = removeFD(functionalDependency, index)
            }
        }
    }
    private fun removeFD(source: MutableList<MutableList<String>>, index: Int): MutableList<MutableList<String>>{
        source[0].removeAt(index)
        source[1].removeAt(index)
        return source
    }
    fun output(){
        for(i in 0 until functionalDependency[0].size){
            println("${functionalDependency[0][i]} -> ${functionalDependency[1][i]}")
        }
    }
}
