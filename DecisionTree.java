import java.util.ArrayList;
public class DecisionTree {
    private Tree tree;
    private int feature_num = -1;
    public DecisionTree(){
        this.tree = new Tree();
    }
    public void fit(int[][] train_x,int[] train_y){
        ArrayList<Object> x = toArrayList_x(train_x);
        ArrayList<Object> y = toArrayList_y(train_y);
        int x_num = x.size();
        int y_num = y.size();
        if(x_num != y_num){
            System.out.println("could not matching x to y!");
        }else{
            Datas datas = new Datas(x,y);
            this.feature_num = datas.featureNumber();
            this.tree.fit(datas);
        }
    }
    public void predict(int[][] test_x,int[] test_y){
        ArrayList<Object> t_x = toArrayList_x(test_x);
        Datas datas = new Datas(t_x,null);
        int feature_num = datas.featureNumber();
        int datas_num = datas.datasNumber();
        int y_num = test_y.length;
        if(this.feature_num != feature_num){
            System.out.println("could not matching features!");
        }else if(datas_num != y_num){
            System.out.println("could not matching x to y!");
        }else{
            ArrayList<Integer> res = this.tree.predict(datas);
            int size = res.size();
            System.out.println("result:");
            for(int i = 0;i < size;i++){
                System.out.print(res.get(i)+"\t");
            }
            System.out.println();
            int t_num = 0;
            for(int i = 0;i < size;i++){
                if(res.get(i) == test_y[i]){
                    t_num++;
                }
            }
            double accuracy = (double)t_num/(double)size;
            System.out.println("accuracy:" + accuracy*100 + "%");
        }
    }

    public void predict(int[][] test_x){
        ArrayList<Object> t_x = toArrayList_x(test_x);
        Datas datas = new Datas(t_x,null);
        int feature_num = datas.featureNumber();
        if(this.feature_num != feature_num){
            System.out.println("could not matching features!");
        }else{
            ArrayList<Integer> res = this.tree.predict(datas);
            int size = res.size();
            System.out.println("result:");
            for(int i = 0;i < size;i++){
                System.out.print(res.get(i)+"\t");
            }
            System.out.println();
        }
    }

    private ArrayList<Object> toArrayList_x(int[][] x){
        ArrayList<Object> arr1 = new ArrayList<>();
        for(int i = 0;i < x.length;i++){
            ArrayList<Integer> arr2 = new ArrayList<>();
            for(int j = 0;j < x[i].length;j++){
                arr2.add(x[i][j]);
            }
            arr1.add(arr2);
        }
        return arr1;
    }
    private ArrayList<Object> toArrayList_y(int[] y){
        ArrayList<Object> arr = new ArrayList<>();
        for(int i = 0;i < y.length;i++){
            arr.add(y[i]);
        }
        return arr;
    }
}
