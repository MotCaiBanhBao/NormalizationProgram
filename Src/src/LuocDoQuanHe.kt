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
            for(temp in right)
                if(baoDong.contains(temp)){
                    val thuocTinhThem = left[right.indexOf(temp)]
                    if(!baoDong.contains(thuocTinhThem)){
                        baoDong += left[right.indexOf(temp)]
                        conThayDoi = true
                    }
                }
        }
        return baoDong
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
        println("All list l ${functionalDependency[0]}")
        println("All list r ${functionalDependency[1]}")
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
        var sizeOfSet = functionalDependency[0].size
        for(temp in 0 until sizeOfSet){
            for (temp2 in findAllSubString(functionalDependency[0][temp].toCharArray())){
                if(timBaoDong(temp2.toString()).contains(functionalDependency[1][temp])){
                    functionalDependency[0][temp] = temp2
                    break
                }
            }
        }
        println("All list l ${functionalDependency[0]}")
        println("All list r ${functionalDependency[1]}")
    }
    fun findAllSubString(array: CharArray): MutableList<String>{
        val arrSize = array.size
        var result: MutableList<String> = mutableListOf()
        var tempString: String = ""
        for (startPoint in 0 until arrSize) {
            for (temp in startPoint until arrSize) {
                for (temp2 in startPoint .. temp){
                    tempString += array[temp2]
                }
                result.add(tempString)
                tempString = ""
            }
        }
        return result
    }
}