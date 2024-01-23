import java.util.ArrayList;

public class Datas {
    //特征
    public ArrayList<Object> features;
    //标签
    public ArrayList<Object> labels;

    public Datas(){}

    public Datas(ArrayList<Object> features,ArrayList<Object> labels){
        this.features = features;
        this.labels = labels;
    }

    //数据总数
    public int datasNumber(){
        return features.size();
    }
    //计算特征数量
    public int featureNumber(){
        if (datasNumber() != 0){
            ArrayList<Integer> feature_list= (ArrayList) this.features.get(0);
            return feature_list.size();
        }
        return -1;
    }
    //计算标签种类数量
    //这个方法返回的是所有标签的种类数量
    public int getClassNumber(){
        ArrayList<Integer> label = labelsClass();
        return label.size();
    }

    //这个方法返回所有类别标签,没有重复
    public ArrayList<Integer> labelsClass(){
        ArrayList<Integer> classLabels = new ArrayList<Integer>();
        for(int i = 0;i< this.labels.size();i++){
            int label = (Integer)this.labels.get(i);
            if(!is_exist(classLabels,label)){
                classLabels.add(label);
            }
        }
        return classLabels;
    }
    //计算每个标签类的元素数量
    public ArrayList<Integer> getEachClassNumber(){
        ArrayList<Integer> label = labelsClass();
        ArrayList<Integer> classcount = new ArrayList<>();
        //存放每个标签的数量
        for(int i = 0;i < label.size();i++){
            int count = 0;
            for(int j = 0;j < this.labels.size();j++){
                if(label.get(i) == this.labels.get(j)){
                    count++;
                }
            }
            classcount.add(count);
        }
        return classcount;
    }
    //判断某个元素是否存在
    private boolean is_exist(ArrayList<Integer> list,int e){
        boolean t = false;
        for(int i = 0;i<list.size();i++){
            if(list.get(i) == e){
                t = true;
            }
        }
        return t;
    }
    //获取第i列特征上的类型,无重复
    public ArrayList<Integer> columnFeatures(int i){
        ArrayList<Integer> column = new ArrayList<>();
        for(int j = 0;j < datasNumber(); j++){
            ArrayList<Integer> arr = (ArrayList<Integer>) this.features.get(j);
            if(!is_exist(column,arr.get(i))){
                column.add(arr.get(i));
            }
        }
        return column;
    }
    //计算第i列上特征值的总数量
    public int getColumnFeatures_num(int i){
        ArrayList<Integer> column = columnFeatures(i);
        return column.size();
    }
    //统计每个特征上各个值的数量
    public ArrayList<Integer> getColumnFeatures_count(int i){
        ArrayList<Integer> column = columnFeatures(i);
        ArrayList<Integer> column_count = new ArrayList<>();
        for(int j = 0;j < column.size();j++){
            int count = 0;
            for (int k = 0;k < datasNumber();k++){
                ArrayList<Integer> arr = (ArrayList<Integer>) this.features.get(k);
                if(column.get(j) == arr.get(i)){
                    count++;
                }
            }
            column_count.add(count);
        }
        return column_count;
    }
    //打印features
    public void printFeatures(){
        for(int i = 0;i < this.features.size();i++){
            ArrayList<Integer> feature_list= (ArrayList) this.features.get(i);
            for(int j = 0;j < featureNumber();j++){
                System.out.print(feature_list.get(j) + "\t");
            }
            System.out.println();
        }
    }
    //打印labels
    public void printLabels(){
        for(int i = 0;i < this.labels.size();i++){
            System.out.print(this.labels.get(i) + "\t");
        }
        System.out.println();
    }
    //对数据按照column中对应不同的特征值进行分割
    public ArrayList<Datas> split(int i){
        //对原始数据进行深copy
        int s_datas_num = this.datasNumber();
        int s_column_num = this.featureNumber();
        ArrayList<Object> copy_features = new ArrayList<>();//深拷贝特征
        for(int j = 0;j < s_datas_num;j++){
            ArrayList<Integer> arr = (ArrayList<Integer>) this.features.get(j); //获取原始列数据
            ArrayList<Integer> copy_arr = new ArrayList<>();
            for(int k = 0;k<s_column_num;k++){
                int s_features_value = arr.get(k);
                copy_arr.add(s_features_value);
            }
            copy_features.add(copy_arr);
        }
        ArrayList<Object> copy_labels = new ArrayList<>();//深拷贝标签
        for(int k = 0;k < s_datas_num;k++){
            int s_label = (Integer) this.labels.get(k);
            copy_labels.add(s_label);
        }
        //需要返回的新数据列表
        ArrayList<Datas> new_DataList = new ArrayList<>();
        //获取第i列中的特征值以及特征值数量
        int column_i_num = getColumnFeatures_num(i);
        ArrayList<Integer> column_i = columnFeatures(i);
        //根据第i列的特征值提取分类后的列表
        for(int k = 0;k < column_i_num;k++){
            //创建新的特征表以及标签表
            ArrayList<Object> new_features = new ArrayList<>();
            ArrayList<Object> new_labels = new ArrayList<>();
            for(int j = 0;j<datasNumber();j++){
                ArrayList<Integer> arr = (ArrayList<Integer>) copy_features.get(j);
                int f_t = arr.get(i);//获取到第i列的特征值
                int label = (Integer) copy_labels.get(j);
                if(f_t == column_i.get(k)){
                    //将相同的特征值的数据加入到新的列表中
                    new_features.add(arr);
                    new_labels.add(label);
                }
            }
            Datas new_datas = new Datas(new_features,new_labels);
            new_DataList.add(new_datas);
        }
        return new_DataList;
    }
    //以第i列分割数据，并且删除第i列
    public ArrayList<Datas> del_split(int i){
       ArrayList<Datas> data_split = split(i);
       int len = data_split.size();
       for(int j = 0;j < len;j++){
           Datas sw_datas = data_split.get(j);
           int num = sw_datas.datasNumber();
           for(int k =0; k< num;k++){
                ArrayList<Integer> arr= (ArrayList<Integer>) sw_datas.features.get(k);
                arr.remove(i);
           }
       }
        return data_split;
    }
}
