// This file is autogenerated: please see the codegen template "Options"
#ifndef PDFTRON_H_CPPPDFOptionsBase
#define PDFTRON_H_CPPPDFOptionsBase

#include <SDF/ObjSet.h>
#include <PDF/ColorSpace.h>
#include <PDF/Rect.h>
#include <PDF/RectCollection.h>

namespace pdftron{ namespace PDF{ 

class OptionsBase
{
public:
	static SDF::Obj GetArray(SDF::Obj dict, const char* key)
	{
		SDF::Obj found = dict.FindObj(key);
		if(!found.IsValid())
		{
			found = dict.PutArray(key);
		}
		return found;
	}
	
	static void PutNumber(SDF::Obj dict, const char* key, double num)
	{
		dict.PutNumber(key, num);
	}
	static void PutBool(SDF::Obj dict, const char* key, bool val)
	{
		dict.PutBool(key, val);
	}
	static void PutText(SDF::Obj dict, const char* key, const UString& text)
	{
		dict.PutText(key, text);
	}
	static void PutRect(SDF::Obj dict, const char* key, const Rect& rect)
	{
		dict.PutRect(key, rect.GetX1(), rect.GetY1(), rect.GetX2(), rect.GetY2());
	}
	
	static void PushBackNumber(SDF::Obj dict, const char* key, double num)
	{
		SDF::Obj arr = GetArray(dict, key);
		arr.PushBackNumber(num);
	}
	static void PushBackBool(SDF::Obj dict, const char* key, bool val)
	{
		SDF::Obj arr = GetArray(dict, key);
		arr.PushBackBool(val);
	}
	static void PushBackText(SDF::Obj dict, const char* key, const UString& text)
	{
		SDF::Obj arr = GetArray(dict, key);
		arr.PushBackText(text);
	}
	static void PushBackRect(SDF::Obj dict, const char* key, const Rect& rect)
	{
		SDF::Obj arr = GetArray(dict, key);
		arr.PushBackRect(rect.GetX1(), rect.GetY1(), rect.GetX2(), rect.GetY2());
	}
	
	static Rect RectFromArray(SDF::Obj nums)
	{
		return Rect(nums.GetAt(0).GetNumber(), nums.GetAt(1).GetNumber(),
			nums.GetAt(2).GetNumber(), nums.GetAt(3).GetNumber());
	}
	
	static void InsertRectCollection(SDF::Obj dict, const char* key,
		const RectCollection& rects, int index)
	{
		SDF::Obj arr = GetArray(dict, key);
		while(arr.Size() <= index)
		{
			arr.PushBackArray();
		}
		SDF::Obj innerArray = arr.GetAt(index);
		for (size_t i = 0; i < rects.GetNumRects(); ++i)
		{
			Rect rect = rects.GetRectAt((int)i);
			innerArray.PushBackRect(rect.GetX1(), rect.GetY1(), rect.GetX2(), rect.GetY2());
		}
	}

	static Rect RectFromArray(void*)
	{
		return Rect();
	}
	
	static double ColorPtToNumber(const ColorPt& cp) 
	{
		int num_comp = 3;
		int red = 0;
		int green = 0;
		int blue = 0;
		int alpha = 255;
		if(num_comp == 1) {
			red = (int)(255*cp.Get(0));
			green = red;
			blue = red;
		}
		else if(num_comp == 3 || num_comp == 4) {
			red = (int)(255*cp.Get(0));
			green = (int)(255*cp.Get(1));
			blue = (int)(255*cp.Get(2));
		}
		if(num_comp == 4) {
			alpha = (int)(255*cp.Get(3));
		}
		UInt32 num = ((0xFF&alpha) << 24)
				| ((0xFF&red) << 16)
				| ((0xFF&green) << 8)
				| ((0xFF&blue) << 0);
		return (double)num;
	}
	static ColorPt ColorPtFromNumber(double dnum) 
	{
		UInt32 num = (UInt32)dnum;
		return ColorPt(((num >> 16) & 0xFF)/255.0,
			((num >> 8) & 0xFF)/255.0,
			((num >> 0) & 0xFF)/255.0,
			((num >> 24)&0xFF)/255.0);
	}
};

}
}

#endif // PDFTRON_H_CPPPDFOptionsBase
