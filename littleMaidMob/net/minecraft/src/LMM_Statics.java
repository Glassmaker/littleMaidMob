package net.minecraft.src;

public class LMM_Statics {

	/** Absoption効果をクライアント側へ転送するのに使う
	 * To use it to transfer to the client side effect Absoption 
	 */
	protected static final int dataWatch_Absoption		= 18;
	
	/** メイドカラー(byte), Made color (byte) */
	protected static final int dataWatch_Color			= 19;
	/**
	 * MSB|0x0000 0000|LSB<br>
	 *       |    |本体のテクスチャインデックス<br>
	 *       |アーマーのテクスチャインデックス<br>
	 *       Texture index of body<br>
	 *       Texture index of Armor
	 */
	protected static final int dataWatch_Texture		= 20;
	/** 
	 * モデルパーツの表示フラグ(Integer)<br>
	 * Display flag of model parts (Integer)
	 */
	protected static final int dataWatch_Parts			= 21;
	/**
	 * 各種フラグを一纏めにしたもの。<br>
	 * Those collectively various flags.
	 */
	protected static final int dataWatch_Flags			= 22;
	protected static final int dataWatch_Flags_looksWithInterest		= 0x00000001;
	protected static final int dataWatch_Flags_looksWithInterestAXIS	= 0x00000002;
	protected static final int dataWatch_Flags_Aimebow					= 0x00000004;
	protected static final int dataWatch_Flags_Freedom					= 0x00000008;
	protected static final int dataWatch_Flags_Tracer					= 0x00000010;
	protected static final int dataWatch_Flags_remainsContract			= 0x00000020;
	protected static final int dataWatch_Flags_PlayingMode				= 0x00000040;
	protected static final int dataWatch_Flags_Working					= 0x00000080;
	protected static final int dataWatch_Flags_Wait						= 0x00000100;
	protected static final int dataWatch_Flags_WaitEx					= 0x00000200;
	protected static final int dataWatch_Flags_LooksSugar				= 0x00000400;
	protected static final int dataWatch_Flags_Bloodsuck				= 0x00000800;
	protected static final int dataWatch_Flags_OverDrive				= 0x00001000;
	/** 
	 * 紐の持ち主のEntityID。<br>
	 * EntityID of the owner of the string
	 */
	protected static final int dataWatch_Gotcha			= 23;
	
	/** 
	 * メイドモード(Short)<br>
	 * Maid mode(Short)
	 */
	protected static final int dataWatch_Mode			= 24;
	
	/**
	 *  利き腕(Byte)<br>
	 *  Dominant arm(Byte)
	 */
	protected static final int dataWatch_DominamtArm	= 25;
	
	/** 
	 * アイテムの使用判定、腕毎(Integer)<br>
	 * Use determination of items, each arm (Integer)
	 */
	protected static final int dataWatch_ItemUse		= 26;
	
	/** 保持経験値、実のところクライアント側では必要ないので要らない(Integer)<br>
	 * I do not want<br>
	 *  because it is not required on the client side<br>
	 *  where retention experience, the real (Integer)
	 */
	protected static final int dataWatch_ExpValue		= 27;
	
	
	
	/**
	 * 自由設定値。<br>
	 * Free setting.
	 */
	protected static final int dataWatch_Free			= 31;
	
	protected static final int dataFlags_ForceUpdateInventory	= 0x80000000;

// NetWork

	/*
	 * 動作用定数、8bit目を立てるとEntity要求
	 * Entity request and make constant operation, the first 8bit
	 */
	
	/*
	 * LMMPacetのフォーマット
	 * Format of LMMPacet
	 * (Byte)
	 * 0	: 識別, Identification(1byte)
	 * 1 - 4: EntityID(4Byte)場合に寄っては省略, Omit to stop by in the case
	 * 5 - 	: Data
	 * 
	 */
	/**
	 * サーバー側へ対象のインベントリを送信するように指示する。
	 * スポーン時点ではインベントリ情報が無いため。
	 * I will be instructed to send an inventory of the object to the server side.
	 * Since there is no inventory information in the spawn point.
	 * [0]		: 0x00;
	 * [1..4]	: EntityID(int);
	 */
	public static final byte LMN_Server_UpdateSlots		= (byte)0x80;
	/**
	 * クライアント側へ腕振りを指示する。
	 * 振った時の再生音声も指定する。
	 * I direct the arm swing to the client side.
	 * Also specify the playback sound when shaken.
	 * [0]		: 0x81;
	 * [1..4]	: EntityID(int);
	 * [5]		: ArmIndex(byte);
	 * [6..9]	: SoundIndex(int);
	 */
	public static final byte LMN_Client_SwingArm		= (byte)0x81;
	/**
	 * サーバー側へ染料の使用を通知する。
	 * GUISelect用。
	 * I will notice the use of the dye to the server side.
	 * GUISelect for.
	 * [0]		: 0x02;
	 * [1]		: color(byte);
	 */
	public static final byte LMN_Server_DecDyePowder	= (byte)0x02;
	/**
	 * サーバーへIFFの設定値が変更されたことを通知する。
	 * I will notice that the setting value of IFF has been changed to the server.
	 * [0]		: 0x04;
	 * [1]		: IFFValue(byte);
	 * [2..5]	: Index(int);
	 * [6..]	: TargetName(str);
	 */
	public static final byte LMN_Server_SetIFFValue		= (byte)0x04;
	/**
	 * クライアントへIFFの設定値を通知する。
	 * I inform the setting of the IFF to the client.
	 * [0]		: 0x04;
	 * [1]		: IFFValue(byte);
	 * [2..5]	: Index(int);
	 */
	public static final byte LMN_Client_SetIFFValue		= (byte)0x04;
	/**
	 * サーバーへ現在のIFFの設定値を要求する。
	 * 要求時は一意な識別番号を付与すること。
	 * I want to request the setting of the IFF to the current server.
	 * And giving a unique identification number request.
	 * [0]		: 0x05;
	 * [1..4]	: Index(int);
	 * [5..]	: TargetName(str);
	 */
	public static final byte LMN_Server_GetIFFValue		= (byte)0x05;
	/**
	 * サーバーへIFFの設定値を保存するように指示する。
	 * Will be prompted to save the settings of the IFF to the server.
	 * [0]		: 0x06;
	 */
	public static final byte LMN_Server_SaveIFF			= (byte)0x06;
	/**
	 * クライアント側へ音声を発生させるように指示する。
	 * 音声の自体はクライアント側の登録音声を使用するため標準の再生手順だと音がでないため。
	 * I instruct to generate a voice to the client side.
	 * Itself the voice for the Sound is not that it is playing the standard
	 * procedure for using the registered voice of the client-side.
	 * [0]		: 0x07;
	 * [1..4]	: EntityID(int);
	 * [5..8]	: SoundIndex(int);
	 */
	public static final byte LMN_Client_PlaySound		= (byte)0x89;


}
