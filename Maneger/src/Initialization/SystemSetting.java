package Initialization;

public class SystemSetting {
	
	/* Total number of processors or cores available to the JVM */
	public int AvailableProcessors = 0;
	
	 /* Total amount of free memory available to the JVM in bytes */
	public long FreeMemory = 0;
	
	 /* Maximum amount of memory the JVM will attempt to use in bytes */
	public long MaxMemory = 0;
	
	/* Total memory currently available to the JVM in bytes*/
	public long TotalMemory = 0;
	
	public SystemSetting(int cores, long free,long max,long total){
		this.AvailableProcessors = cores;
		this.FreeMemory = free;
		this.MaxMemory = max;
		this.TotalMemory = total;
	}
	
}
