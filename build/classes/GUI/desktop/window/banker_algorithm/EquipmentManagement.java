package GUI.desktop.window.banker_algorithm;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class EquipmentManagement {

    private int[] _Resource = new int[3];
    private int[] _Available = new int[3];
    private int[][] _Claim = new int[15][3];
    private int[][] _Allocation = new int[15][3];
    private int sum;
    private int num;

    public EquipmentManagement() {
        for(int i=0;i<3;i++){
            this._Resource[i] = 40;
            this._Available[i] = this._Resource[i];
        }
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 3; j++)
                this._Claim[i][j] = (int) (Math.random() * 5) * ((int) (Math.random() * 5));
            if((this._Claim[i][0]+this._Claim[i][1]+this._Claim[i][2])==0)
                i--;
        }
        this.sum = 0;
        this.num = 0;
    }

    /** 对所有进程进行设备申请，返回通过申请的进程数量 */
    public boolean application(int i) {
        if (i < 15) {
            this.setNum(this.getNum() + 1);
            if (this.getClaim(i)[0] <= this.getAvailable(0) && this.getClaim(i)[1] <= this.getAvailable(1) && this.getClaim(i)[2] <= this.getAvailable(2)) {
                this.setAllocation(getSum(), this.getClaim(i));
                int[] available = new int[3];
                available[0] = this.getAvailable(0) - this.getClaim(i)[0];
                available[1] = this.getAvailable(1) - this.getClaim(i)[1];
                available[2] = this.getAvailable(2) - this.getClaim(i)[2];
                this.setAvailable(available);
                this.setSum(this.getSum() + 1);
                return true;
            }
        }
        return false;
    }

    public String ToString(int[] number) {
        String str = "";
        str = str + number[0] + "  " + number[1] + "  " + number[2];
        return str;
    }

    /**
     * @返回进程总数量
     */
    public int getClaimLength() {
        return _Claim.length;
    }

    /**
     * @return the _Resource
     */
    public int getResource(int number) {
        return _Resource[number];
    }

    public int[] getResource() {
        return _Resource;
    }

    /**
     * @param Resource the _Resource to set
     */
    public void setResource(int[] Resource) {
        this._Resource = Resource;
    }

    /**
     * @return the _Available[i]
     */
    public int getAvailable(int number) {
        return _Available[number];
    }

    public int[] getAvailable() {
        return _Available;
    }

    /**
     * @param Available the _Available to set
     */
    public void setAvailable(int[] Available) {
        this._Available = Available;
    }

    /**
     * @return the _Claim
     */
    public int[] getClaim(int number) {
        return _Claim[number];
    }

    public int[][] getClaim() {
        return _Claim;
    }

    /**
     * @param Claim the _Claim to set
     */
    public void setClaim(int number, int[] Claim) {
        this._Claim[number] = Claim;
    }

    /**
     * @return the _Allocation
     */
    public int[] getAllocation(int number) {
        return _Allocation[number];
    }

    public int[][] getAllocation() {
        return _Allocation;
    }

    /**
     * @param Allocation the _Allocation to set
     */
    public void setAllocation(int number, int[] Allocation) {
        this._Allocation[number] = Allocation;
    }

    /**
     * @return the sum
     */
    public int getSum() {
        return sum;
    }

    /**
     * @param sum the sum to set
     */
    public void setSum(int sum) {
        this.sum = sum;
    }

    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num the num to set
     */
    public void setNum(int num) {
        this.num = num;
    }
}
