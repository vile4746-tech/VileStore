Shader "Hidden/Miaobian"
{


    Properties
    {
       _max ("Threshold",Range(0,1)) = 0.2
       _color ("Color",Color) = (0,0,0,1)
    }
    SubShader
    {
        Tags { "RenderType" = "Opaque" "RenderPipeline" = "UniversalPipeline" }
        Pass
        {

            HLSLPROGRAM
            #pragma vertex Vert
            #pragma fragment Frag
            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/Core.hlsl"
            #include "Packages/com.unity.render-pipelines.core/Runtime/Utilities/Blit.hlsl"
            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/DeclareDepthTexture.hlsl"

            //TEXTURE2D(_CameraDepthTexture);
            //TEXTURE2D(_CameraColorTexture);
            //SAMPLER(sampler_PointClamp);

            float _max;
            half4 _color;

            float4 _CameraDepthTexture_TexelSize;

            float SampleDepth(float2 uv){
                return SampleSceneDepth(uv);
                //return SAMPLE_TEXTURE2D(_CameraDepthTexture,sampler_PointClamp,uv).r;
            }

            float check(float2 uv){
                float2 texelSize = _CameraDepthTexture_TexelSize.xy;
                float d[9];
                d[0] = SampleDepth(uv + float2(-texelSize.x,-texelSize.y));
                d[1] = SampleDepth(uv + float2(0,-texelSize.y));
                d[2] = SampleDepth(uv + float2(texelSize.x,-texelSize.y));;
                d[3] = SampleDepth(uv + float2(-texelSize.x,0));;
                d[4] = SampleDepth(uv + float2(0,0));;
                d[5] = SampleDepth(uv + float2(texelSize.x,0));;
                d[6] = SampleDepth(uv + float2(-texelSize.x,texelSize.y));;
                d[7] = SampleDepth(uv + float2(0,texelSize.y));;
                d[8] = SampleDepth(uv + float2(texelSize.x,texelSize.y));;

                float gx = -d[0] + d[2] -2*d[3] +2*d[5] - d[6] + d[8];
                float gy = -d[0] -2*d[1] - d[2] + d[6] + 2*d[7] + d[8];

                return saturate(sqrt(gx*gx+gy*gy));

            }
            //Varyings
            half4 Frag(Varyings i) : SV_Target
            {
                half4 d = SAMPLE_TEXTURE2D(_BlitTexture,sampler_LinearRepeat,i.texcoord);
                float edge = check(i.texcoord);


               
                if(edge > _max) return _color;
                else return d;


                //half4 final = lerp(d,_color,step(_max,edge));
                //return final;
                //return half4(1, 0, 0, 1); // È«ÆÁºìÉ«
            }
            ENDHLSL
        }
    }
}
/*

Shader "Hidden/test"
{
    Properties
    {
       _max ("Threshold",Range(0,1)) = 0.2
       _color ("Color",Color) = (0,0,0,1)
    }
    SubShader
    {
        Tags { "RenderType" = "Opaque" "RenderPipeline" = "UniversalPipeline" }
        Pass
        {
            Name "FullScreenBlit"

            HLSLPROGRAM
            #pragma vertex Vert
            #pragma fragment Frag
            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/Core.hlsl"
            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/DeclareDepthTexture.hlsl"
            #include "Packages/com.unity.render-pipelines.core/Runtime/Utilities/Blit.hlsl"

            float _max;
            half4 _color;

            float SampleDepth(float2 uv){
                return SampleSceneDepth(uv);
            }

            float check(float2 uv){
                float2 texelSize = _CameraDepthTexture_TexelSize.xy;
                float d[9];
                d[0] = SampleDepth(uv + float2(-texelSize.x,-texelSize.y));
                d[1] = SampleDepth(uv + float2(0,-texelSize.y));
                d[2] = SampleDepth(uv + float2(texelSize.x,-texelSize.y));
                d[3] = SampleDepth(uv + float2(-texelSize.x,0));
                d[4] = SampleDepth(uv + float2(0,0));
                d[5] = SampleDepth(uv + float2(texelSize.x,0));
                d[6] = SampleDepth(uv + float2(-texelSize.x,texelSize.y));
                d[7] = SampleDepth(uv + float2(0,texelSize.y));
                d[8] = SampleDepth(uv + float2(texelSize.x,texelSize.y));

                float gx = -d[0] + d[2] -2*d[3] +2*d[5] - d[6] + d[8];
                float gy = -d[0] -2*d[1] - d[2] + d[6] + 2*d[7] + d[8];

                return saturate(sqrt(gx*gx+gy*gy));
            }

            half4 Frag(Varyings i) : SV_Target
            {
                half4 d = SAMPLE_TEXTURE2D(_BlitTexture,sampler_LinearRepeat,i.texcoord);
                float edge = check(i.texcoord);

                if(edge > _max) return _color;
                else return d;
            }
            ENDHLSL
        }
    }
}


*/