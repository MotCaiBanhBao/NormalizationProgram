class LuocDoQuanHe(leftList: MutableList<String>, u: String, rightList: MutableList<String>) {
    val u: String = u
    val left = leftList
    val right = rightList
    val functionalDependency = mutableListOf<MutableList<String>>(leftList, rightList)

    fun timBaoDong(thuocTinhCanTim:String):String{
        var conThayDoi = true
        var baoDong = thuocTinhCanTim
        while (conThayDoi){
            conThayDoi = false;
            for(temp in left)
                if(isContains(baoDong, temp)){
                    val thuocTinhThem = right[left.indexOf(temp)]
                    if(!isContains(baoDong, thuocTinhThem)){
                        baoDong = addAtribute(baoDong, thuocTinhThem)
                        conThayDoi = true
                    }
                }
        }
        return baoDong
    }
    private fun addAtribute(source: String, atribute: String): String{
        var result = source
        var atributeArray = atribute.toCharArray()
        for(oneAtribute in atributeArray)
            if(!result.contains(oneAtribute))
                result += oneAtribute
        return result
    }
     fun isContains(Container: String, needCheckString: String): Boolean{
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
        for(i in 0 until functionalDependency[0].size){
            println("${functionalDependency[0][i]} -> ${functionalDependency[1][i]}")
        }
    }
    fun convertRHS(index: Int){
        for(temp in (functionalDependency[1][index]).toCharArray()){
            functionalDependency[0].add(functionalDependency[0][index])
            functionalDependency[1].add(temp.toString())
        }
        functionalDependency[0].removeAt(index)
        functionalDependency[1].removeAt(index)

    }
    fun removeDuplicate(){
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
        var tempFunctionalDependency : MutableList<MutableList<String>> = mutableListOf()
        tempFunctionalDependency.addAll(functionalDependency)
        var sizeOfSet = tempFunctionalDependency[0].size
        for(temp in 0 until sizeOfSet){
            var subString = findAllSubString(tempFunctionalDependency[0][temp])
            for (temp2 in subString){
                if(isContains(timBaoDong(temp2), tempFunctionalDependency[1][temp])){
                    tempFunctionalDependency[0][temp] = temp2
                    break
                }
            }
        }
        for(i in 0 until sizeOfSet){
            println("${tempFunctionalDependency[0][i]} -> ${tempFunctionalDependency[1][i]}")
        }
    }
    fun findAllSubString(source: String): MutableList<String>{
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
}
