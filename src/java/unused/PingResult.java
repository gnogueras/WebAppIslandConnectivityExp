/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unused;

/**
 *
 * @author gerard
 */
public class PingResult {
    
    String ts, rtt;
    int tx, rx;
    double lossrate;

    public PingResult() {
    }

    public PingResult(String ts, String rtt, int tx, int rx, double lossrate) {
        this.ts = ts;
        this.rtt = rtt;
        this.tx = tx;
        this.rx = rx;
        this.lossrate = lossrate;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getRtt() {
        return rtt;
    }

    public void setRtt(String rtt) {
        this.rtt = rtt;
    }

    public int getTx() {
        return tx;
    }

    public void setTx(int tx) {
        this.tx = tx;
    }

    public int getRx() {
        return rx;
    }

    public void setRx(int rx) {
        this.rx = rx;
    }

    public double getLossrate() {
        return lossrate;
    }

    public void setLossrate(double lossrate) {
        this.lossrate = lossrate;
    }
    
    
    
}
