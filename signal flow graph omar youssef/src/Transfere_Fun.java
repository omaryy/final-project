
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Transfere_Fun {
	
	private ArrayList<ArrayList <Node>>     edges;
	private ArrayList<ArrayList <Integer>>  gains;
	
	public ArrayList<int[]> f_paths;
	public ArrayList<int[]> loops  ;
	
	public int[] forward_gains;
	public int[] loop_gains   ;
	
	private Stack<Integer> s;
	public boolean[][] touching;
	
	public ArrayList<Integer> two_non;   
	public ArrayList<Integer> three_non; 
	public ArrayList<Integer> four_non;
	
	public ArrayList<Integer> two_nont = new ArrayList<Integer>();   
	public ArrayList<Integer> three_nont = new ArrayList<Integer>(); 
	public ArrayList<Integer> four_nont = new ArrayList<Integer>();
	
	private int delta;
	private ArrayList<Integer> path_deltas;
	
	public Transfere_Fun(ArrayList<ArrayList <Node>> edges, ArrayList<ArrayList <Integer>>  gains) {
		this.edges = edges;
		this.gains = gains;
	}
	
	public ArrayList<int[]> get_forward_paths() {
		return f_paths;
	}

	public ArrayList<int[]> get_loops() {
		return loops;
	}

	public int[] getForward_gains() {
		return forward_gains;
	}

	public ArrayList<Integer> getTwo_non() {
		return two_non;
	}

	public ArrayList<Integer> getThree_non() {
		return three_non;
	}

	public ArrayList<Integer> getFour_non() {
		return four_non;
	}

	public int get_delta() {
		return delta;
	}

	public ArrayList<Integer> get_Path_deltas() {
		return path_deltas;
	}

	public double solve(){
		
		f_paths = new ArrayList<int[]>(); 
		loops   = new ArrayList<int[]>();
		
		find_forward();
		find_loops()  ;
		touch_loops() ;
		
		return compute();
	}
	
	private double compute(){
		
		double sum = 0;
		
		compute_total_delta();
		compute_deltas();

		for(int i = 0; i < f_paths.size(); i++)
			sum += forward_gains[i] * path_deltas.get(i);
		return (sum / delta); 
	}
	
	private void find_forward(){
		Stack<Integer> s = new Stack<Integer>();
		//find forward paths
		//f_paths will be built
		find_path(s, 0, edges.size() - 1, f_paths);
		if(f_paths.size() == 0){
			System.out.println("Invalid Graph");
			return;
		}
		forward_gains = new int[f_paths.size()];
		//find forward gains
		//forward_gains will be built
		for(int i = 0; i < f_paths.size(); i++){
			int[] path = f_paths.get(i);
			forward_gains[i] = 1;
			for(int j = 0; j < path.length - 1; j++){
				int from  = path[j];
				int to    = find_index(path[j], path[j + 1]);
				forward_gains[i] *= gains.get(from).get(to);
			}
		}
	}
	
	private void find_loops(){
		Stack<Integer> s = new Stack<Integer>();
		//find loops
		//loops will be built
		for(int i = 0; i < edges.size(); i++)
			find_path(s, i, i, loops);
		loop_gains    = new int[loops.size()]  ;
		//find loop gains
		//loop_gains will be built
		for(int i = 0; i < loops.size(); i++){
			int[] loop = loops.get(i);
			loop_gains[i] = 1;
			for(int j = 0; j < loop.length - 1; j++){
				int from  = loop[j];
				int to    = find_index(loop[j], loop[j + 1]);
				loop_gains[i] *= gains.get(from).get(to);
			}
		}
	}
	
	private void find_path(Stack<Integer> s,int label, int end, ArrayList<int[]> arrayList){
		if(s.search(label) == -1) {
			s.push(label);
			ArrayList<Node> list = edges.get(label);
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).label != end)
					find_path(s, list.get(i).label, end, arrayList);
				else
					store_path(s, end, arrayList);
			}
			s.pop();
		}
	}
	
	private void store_path(Stack<Integer> s, int end, ArrayList<int[]> arrayList){
		int[] p = new int[s.size() + 1];
		p[s.size()] = end;
		for(int i = s.size() - 1; i >= 0; i--)
			p[i] = s.pop();
		for(int i = 0; i < p.length - 1; i++)
			s.push(p[i]);
		if(arrayList == f_paths || !is_found(p))
			arrayList.add(p);
	}
	
	private boolean is_found(int[] p) {
		for(int i = 0; i < loops.size(); i++){
			if(p.length == loops.get(i).length){
				if(compare(p, loops.get(i)))
					return true;
			}
		}
		return false;
	}
	
	private boolean compare(int[] p, int[] k){
		boolean flag;
		for(int j = 0; j < p.length; j++){
			flag = false;
			for(int i = 0; i < p.length; i++){
				if(p[j] == k[i])
					flag = true;
			}
			if(flag == false)
				return false;
		}
		return true;
	}
	
	private int find_index(int from, int to){
		ArrayList<Node> arrayList = edges.get(from);
		for(int i = 0; i < arrayList.size(); i++){
			if(arrayList.get(i).label == to)
				return i;
		}
		return -1;
	}
	
	private void touch_loops(){
		int rows = loops.size() + f_paths.size();
		int cols = loops.size();
		touching = new boolean[rows][cols];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				if(i < cols){
					if(j < i)
						touching[i][j] = touching[j][i];
					else
						touching[i][j] = check(loops.get(i), loops.get(j));
				}
				else
					touching[i][j] = check(f_paths.get(i - cols), loops.get(j));
				}
			}
	}
	
	private boolean check(int[] arr1, int []arr2){
		for(int j = 0; j < arr1.length; j++){
			for(int i = 0; i < arr2.length; i++){
				if(arr1[j] == arr2[i])
					return true;
			}
		}
		return false;
	}
	
	public void compute_total_delta(){
		get_non_touching();
		int sum_1 = 0;
		int sum_2 = 0;
		int sum_3 = 0;
		int sum_4 = 0;
		for(int i = 0; i < loop_gains.length; i++)
			sum_1 += loop_gains[i];
		for(int i = 0; i < two_non.size(); i++)
			sum_2 += two_non.get(i);
		for(int i = 0; i < three_non.size(); i++)
			sum_3 += three_non.get(i);
		for(int i = 0; i < four_non.size(); i++)
			sum_4 += four_non.get(i);
		delta = (1 - sum_1 + sum_2 - sum_3 + sum_4);
	}
	
	private void get_non_touching(){

		two_non   = new ArrayList<Integer>();
		three_non = new ArrayList<Integer>();
		four_non  = new ArrayList<Integer>();
	
		ArrayList<Integer> flags;
		
		// 2-non touching loops
		for(int i = 0; i < loops.size() - 1; i++){
			for(int j = i + 1; j < loops.size(); j++){
				if(touching[i][j] == false){
					int gain = loop_gains[i] * loop_gains[j];
					two_nont.add(i);
					two_nont.add(j);
					two_non.add(gain);
				}
			}
		}
		
		// 3-non touching loops
		for(int i = 0; i < loops.size() - 1; i++){
			flags = new ArrayList<Integer>();
			for(int j = i + 1; j < loops.size(); j++){
				if(touching[i][j] == false)
					flags.add(j);
			}
			if(flags.size() > 1){
				for(int s = 0; s < flags.size() - 1; s++){
					int start = flags.get(s);
					for(int k = s + 1; k < flags.size(); k++){
						int end   = flags.get(k);
						if(touching[start][end] == false){
							int gain = loop_gains[i] * loop_gains[start] * loop_gains[end];
							three_nont.add(i);
							three_nont.add(start);
							three_nont.add(end);
							three_non.add(gain);
						}
					}
				}
			}
		}
		
		// 4-non touching loops
		for(int i = 0; i < loops.size() - 1; i++){
			flags = new ArrayList<Integer>();
			for(int j = i + 1; j < loops.size(); j++){
				if(touching[i][j] == false)
					flags.add(j);
			}
			if(flags.size() > 2){
				for(int s = 0; s < flags.size() - 2; s++){
					int start = flags.get(s);
					for(int l = s + 1; l < flags.size() - 1; l++){
						int med   = flags.get(l);
						for(int k = l + 1; k < flags.size(); k++){
							int end   = flags.get(k);
							if(touching[start][med] == false && touching[start][end] == false && touching[med][end] == false ){
								int gain = loop_gains[i] * loop_gains[start] * loop_gains[med] * loop_gains[end];
								four_nont.add(i);
								four_nont.add(start);
								four_nont.add(med);
								four_nont.add(end);
								four_non.add(gain);
							}
						}
					}
				}
			}
		}
	}

	private void compute_deltas(){
		
		path_deltas = new ArrayList<Integer>();
		
		ArrayList<Integer> list;
		for(int i = loops.size(); i < loops.size() + f_paths.size(); i++){
			list = new ArrayList<Integer>();
			for(int j = 0; j < loops.size(); j++)
				if(touching[i][j] == false)
					list.add(j);
			path_deltas.add(get_path_delta(list));	
		}
	}
	
	private int get_path_delta(ArrayList<Integer> list){
		ArrayList<Integer> two   = new ArrayList<Integer>();
		ArrayList<Integer> three = new ArrayList<Integer>();
		ArrayList<Integer> four  = new ArrayList<Integer>();
		
		ArrayList<Integer> flags;
		
		// 2-non touching loops
		if(list.size() > 1){
			for(int i = 0; i < list.size() - 1; i++){
				int start = list.get(i);
				for(int j = i + 1; j < loops.size(); j++){
					int end = list.get(j);
					if(touching[start][end] == false){
						int gain = loop_gains[start] * loop_gains[end];
						two.add(gain);
					}
				}
			}
		}
		// 3-non touching loops
		if(list.size() > 2){
			for(int i = 0; i < list.size() - 2; i++){
				int start = list.get(i);
				for(int j = i + 1; j < list.size() - 1; j++){
					int med   = list.get(j);
					for(int k = j + 1; k < list.size(); k++){
						int end   = list.get(k);
						if(touching[start][med] == false && touching[start][end] == false && touching[med][end] == false ){
							int gain = loop_gains[start] * loop_gains[med] * loop_gains[end];
							three.add(gain);
						}
					}
				}
			}
		}
		// 4-non touching loops
		if(list.size() > 3){
			for(int i = 0; i < list.size() - 3; i++){
				int start = list.get(i);
				for(int j = i + 1; j < list.size() - 2; j++){
					int med_1   = list.get(j);
					for(int k = j + 1; k < list.size() - 1; k++){
						int med_2   = list.get(k);
						for(int l = k + 1; l < list.size(); l++){
							int end   = list.get(k);
							if(!(touching[start][med_1] || touching[start][med_2] || touching[start][end] || touching[med_1][med_2] || touching[med_1][end] || touching[med_2][end])){
								int gain = loop_gains[start] * loop_gains[med_1] * loop_gains[med_2] * loop_gains[end];
								four.add(gain);
							}
						}
					}
				}
			}
		}
		
		int sum_1 = 0;
		int sum_2 = 0;
		int sum_3 = 0;
		int sum_4 = 0;
		for(int i = 0; i < list.size(); i++)
			sum_1 += loop_gains[list.get(i)];
		for(int i = 0; i < two.size(); i++)
			sum_2 += two.get(i);
		for(int i = 0; i < three.size(); i++)
			sum_3 += three.get(i);
		for(int i = 0; i < four.size(); i++)
			sum_4 += four.get(i);
		return (1 - sum_1 + sum_2 - sum_3 + sum_4);
	}
}