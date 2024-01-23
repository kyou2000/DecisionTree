import java.util.ArrayList;
import java.util.Stack;

public class Tree {
    Node root = null;
    public Tree(){}
    //构建树,
    public void fit(Datas datas){
        Stack<Node> treeNode_stack = new Stack<>();
        Stack<Datas> datas_stack = new Stack<>();
        if (this.root == null){
            this.root = new Node();
            //计算总体GINI指数
            double o_gini = GINI(datas,-1);
            int feature_num = datas.featureNumber();//特征数量
            double[] GINI_lsit = new double[feature_num];
            for(int i = 0;i < feature_num;i++){
                GINI_lsit[i] = GINI(datas,i);
            }
            //选出基尼指数最小的
            int index = getMinIndex(GINI_lsit);
            //分类后的gini指数
            double rel_gini = GINI_lsit[index];
            if(can_split(o_gini,rel_gini)){
                datas_stack.push(datas);
                treeNode_stack.push(this.root);
            }
        }
            Datas d = null;
            Node p = null;
            //System.out.println(2);
            while(!treeNode_stack.empty()){
                p = treeNode_stack.pop();
                d = datas_stack.pop();
                if(p.is_read){
                    continue;
                }
                double curr_gini = GINI(d,-1);//计算当前的gini指数
                p.gini = curr_gini;
                if(curr_gini == 0){
                    p.is_leave = true;
                    int label = (Integer)d.labels.get(0);
                    p.label = label;
                    continue;
                }
                int feature_number = d.featureNumber();
                double[] GINI_lsit = new double[feature_number];
                for(int i = 0;i < feature_number;i++){
                    GINI_lsit[i] = GINI(d,i);
                }
                int Index = getMinIndex(GINI_lsit);
                if(can_split(curr_gini,GINI_lsit[Index])){
                    int column_feateature_num = d.getColumnFeatures_num(Index);//当前特征分类下共有几种情况
                    ArrayList<Datas> all_datas = d.del_split(Index);//将分类后的所有新数据集返回
                    ArrayList<Integer> feature_column_class = d.columnFeatures(Index);
                    for(int i = 0;i < column_feateature_num;i++){
                        Node new_node = new Node();
                        p.feature = Index;
                        new_node.feature_value = feature_column_class.get(i);
                        p.next.add(new_node);
                        p.is_read = true;
                        treeNode_stack.push(new_node);
                        datas_stack.push(all_datas.get(i));
                    }
                }
            }

    }
    //这个方法返回的是单个基尼指数，传入参数为要切分的数据，以及要切分的特征索引。
    private double GINI(Datas datas,int i){
        ArrayList<Datas> split_datas = null;
        if(i < 0){
            split_datas = new ArrayList<>();
            split_datas.add(datas);
        }
        else{
            split_datas = datas.split(i);
        }
        //以第i列分割数据
        ArrayList<Double> rate_i_c = new ArrayList<>();//保存所有切分后的数据概率
        ArrayList<Double> i_gini = new ArrayList<>();//保存各部分的gini指数
        int s_datas_num = datas.datasNumber();//原始数据的个数
        int length_split_datas = split_datas.size();
        for(int j = 0;j < length_split_datas;j++){
            //获取label
            Datas datas_sw = split_datas.get(j);
            int label_num = datas_sw.datasNumber();//所有标签的总数
            int curr_datas_num = label_num;//现在数据的总个数
            double i_rate = (double)curr_datas_num/(double)s_datas_num;//当前数据概率
            rate_i_c.add(i_rate);
            int diff_label_num = datas_sw.getClassNumber();//包含的不同标签的数量
            ArrayList<Integer> count_labels = datas_sw.getEachClassNumber();//包含的不同标签在数据集中的数量
            double sum = 0;
            for(int k = 0;k < diff_label_num;k++){
                int curr_label = count_labels.get(k);//当前标签的数量
                double rate = (double)curr_label/(double)label_num;//当前标签的概率
                sum+=rate*rate;
            }
            double part_gini = 1 - sum;
            i_gini.add(part_gini);
        }
        //计算总体的GINI指数
        double temp = 0;
        for(int k = 0;k < length_split_datas;k++){
           temp += rate_i_c.get(k)*i_gini.get(k);
        }
        return temp;
    }
    //选出数组中基尼指数最小的,返回索引
    private int getMinIndex(double[] array){
        int len = array.length;
        double min = array[0];
        int index = -1;
        for(int i = 0;i < len;i++){
            if(min > array[i]){
                min = array[i];
                index = i;
            }
        }
        return index;
    }
    //判断该节点是否可以分裂
    private boolean can_split(double o_gini,double curr_gini){
        boolean t = false;
        if(curr_gini - o_gini < 0){
            t = true;
        }
        return t;
    }
    //推测数据的方法
    public ArrayList<Integer> predict(Datas datas){
        int datas_num = datas.datasNumber();
        ArrayList<Integer> res= new ArrayList<>();//保存所有的推测结果
        for(int i = 0; i < datas_num;i++){
            Node p = this.root;
            if(p != null){
                ArrayList<Integer> arr = (ArrayList<Integer>) datas.features.get(i);//数据中对应个体的所有特征
                while(p.next.size() != 0){
                    int feature = p.feature;//获取树结构要分类的特征
                    //从数据中找到对应特征索引的对应信息
                    int ffdatas = arr.get(feature);//获取到特征值
                    //遍历当前节点中下一个节点上记录的具体特征
                    int node_num = p.next.size();
                    for(int j = 0;j < node_num;j++){
                        Node node_next = p.next.get(j);
                        int feature_value = node_next.feature_value;
                        if(ffdatas == feature_value){
                            p = node_next;
                            break;
                        }
                    }
                }
                res.add(p.label);
            }
        }
        return res;
    }
}
